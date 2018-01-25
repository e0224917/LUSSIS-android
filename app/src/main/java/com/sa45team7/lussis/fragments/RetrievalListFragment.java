package com.sa45team7.lussis.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.activities.StationeryDetailActivity;
import com.sa45team7.lussis.adapters.RetrievalAdapter;
import com.sa45team7.lussis.data.UserManager;
import com.sa45team7.lussis.dialogs.AdjustDialog;
import com.sa45team7.lussis.rest.LUSSISClient;
import com.sa45team7.lussis.rest.model.Adjustment;
import com.sa45team7.lussis.rest.model.LUSSISResponse;
import com.sa45team7.lussis.rest.model.RetrievalItem;
import com.sa45team7.lussis.utils.ErrorUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.sa45team7.lussis.dialogs.AdjustDialog.REQUEST_ADJUST;

/**
 * A simple {@link Fragment} subclass.
 */
public class RetrievalListFragment extends Fragment
        implements RetrievalAdapter.OnRetrievalListInteractionListener {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView retrievalListView;

    private String selectedItemNum;

    public RetrievalListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retrieval_list, container, false);
        refreshLayout = view.findViewById(R.id.retrieval_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
            }
        });

        retrievalListView = view.findViewById(R.id.retrieval_list);

        getList();

        return view;
    }

    private void getList() {
        Call<List<RetrievalItem>> call = LUSSISClient.getApiService().getRetrievalList();
        call.enqueue(new Callback<List<RetrievalItem>>() {
            @Override
            public void onResponse(Call<List<RetrievalItem>> call, Response<List<RetrievalItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RetrievalAdapter adapter = new RetrievalAdapter(response.body(),
                            RetrievalListFragment.this);
                    retrievalListView.setAdapter(adapter);
                    checkListEmpty();
                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<RetrievalItem>> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void adjustStock(Adjustment adjustment) {
        Call<LUSSISResponse> call = LUSSISClient.getApiService().stockAdjust(adjustment);
        call.enqueue(new Callback<LUSSISResponse>() {
            @Override
            public void onResponse(Call<LUSSISResponse> call, Response<LUSSISResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(),
                            response.body().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LUSSISResponse> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSelectRetrievalItem(RetrievalItem item) {
        Intent intent = new Intent(getContext(), StationeryDetailActivity.class);
        intent.putExtra("itemNum", item.getItemNum());
        startActivity(intent);
    }

    @Override
    public void onSelectAdjust(RetrievalItem item) {
        selectedItemNum = item.getItemNum();

        AdjustDialog dialog = new AdjustDialog();
        dialog.setTargetFragment(this, REQUEST_ADJUST);
        dialog.show(getFragmentManager(), "adjust_dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADJUST && resultCode == RESULT_OK) {
            Adjustment adjustment = new Adjustment();
            adjustment.setItemNum(selectedItemNum);
            adjustment.setQuantity(data.getIntExtra("number", 0));
            adjustment.setReason(data.getStringExtra("reason"));
            adjustment.setRequestEmpNum(UserManager.getInstance().getCurrentEmployee().getEmpNum());
            adjustStock(adjustment);
        }
    }

    private void checkListEmpty() {
        boolean isEmpty = retrievalListView.getAdapter().getItemCount() == 0;
        retrievalListView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

}
