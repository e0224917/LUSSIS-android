package com.sa45team7.lussis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.adapters.PendingReqAdapter;
import com.sa45team7.lussis.data.UserManager;
import com.sa45team7.lussis.rest.LUSSISClient;
import com.sa45team7.lussis.rest.model.Requisition;
import com.sa45team7.lussis.utils.InternetConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingReqFragment extends Fragment implements PendingReqAdapter.OnPendingReqListInteractionListener {

    private int mColumnCount = 1;

    private RecyclerView pendingReqListView;
    private SwipeRefreshLayout refreshLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PendingReqFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_req, container, false);

        refreshLayout = view.findViewById(R.id.pending_refresh_layout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequisitionsList();
            }
        });

        pendingReqListView = view.findViewById(R.id.pending_req_list);

        pendingReqListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        getRequisitionsList();
        return view;
    }

    private void getRequisitionsList() {
        if (InternetConnection.checkConnection(getContext())) {
            String deptCode = UserManager.getInstance().getCurrentEmployee().getDeptCode();
            Call<List<Requisition>> call = LUSSISClient.getApiService().getPendingRequisitions(deptCode);
            call.enqueue(new Callback<List<Requisition>>() {
                @Override
                public void onResponse(Call<List<Requisition>> call, Response<List<Requisition>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pendingReqListView.setAdapter(new PendingReqAdapter(response.body(), PendingReqFragment.this));
                    } else {

                    }
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<Requisition>> call, Throwable t) {
                    Toast.makeText(getContext(),
                            "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void onSelectRequisition(Requisition item) {

    }

    @Override
    public void onApproveRequisition(Requisition item) {

    }

    @Override
    public void onRejectRequisition(Requisition item) {

    }
}
