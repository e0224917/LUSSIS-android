package com.sa45team7.lussis.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.activities.ScanQRActivity;
import com.sa45team7.lussis.adapters.ReqDetailAdapter;
import com.sa45team7.lussis.data.UserManager;
import com.sa45team7.lussis.rest.LUSSISClient;
import com.sa45team7.lussis.rest.model.Disbursement;
import com.sa45team7.lussis.utils.DateConvertUtil;
import com.sa45team7.lussis.utils.ErrorUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionPointFragment extends Fragment {

    private static final int REQUEST_SCAN = 6;
    private RecyclerView reqDetailListView;
    private SwipeRefreshLayout refreshLayout;
    private TextView dateText;
    private TextView timeText;
    private TextView collectionName;
    private View containerLayout;

    private Disbursement currentDisbursement;

    public CollectionPointFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_point, container, false);

        refreshLayout = view.findViewById(R.id.collection_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpcomingCollection();
            }
        });

        containerLayout = view.findViewById(R.id.collection_container_layout);

        dateText = view.findViewById(R.id.date_text);
        timeText = view.findViewById(R.id.time_text);
        collectionName = view.findViewById(R.id.collection_text);
        reqDetailListView = view.findViewById(R.id.req_detail_list);

        Button acknowledgeButton = view.findViewById(R.id.scan_button);
        acknowledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanQRActivity.class);
                startActivityForResult(intent, REQUEST_SCAN);
            }
        });

        getUpcomingCollection();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SCAN && resultCode == RESULT_OK) {
            currentDisbursement = null;
            checkCollectionEmpty();
        }

    }

    private void getUpcomingCollection() {
        String deptCode = UserManager.getInstance().getCurrentEmployee().getDeptCode();
        Call<Disbursement> call = LUSSISClient.getApiService().getUpcomingCollection(deptCode);

        call.enqueue(new Callback<Disbursement>() {
            @Override
            public void onResponse(Call<Disbursement> call, Response<Disbursement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentDisbursement = response.body();

                    String date = DateConvertUtil.convertForRequisitions(currentDisbursement.getCollectionDate());
                    dateText.setText(date);

                    timeText.setText(currentDisbursement.getCollectionTime());

                    collectionName.setText(currentDisbursement.getCollectionPoint());

                    ReqDetailAdapter adapter = new ReqDetailAdapter(currentDisbursement.getDisbursementDetails());
                    reqDetailListView.setAdapter(adapter);

                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
                checkCollectionEmpty();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Disbursement> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void checkCollectionEmpty() {
        containerLayout.setVisibility(currentDisbursement == null ? View.GONE : View.VISIBLE);
    }
}