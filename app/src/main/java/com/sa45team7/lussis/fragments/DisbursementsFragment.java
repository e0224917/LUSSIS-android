package com.sa45team7.lussis.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.rest.LUSSISClient;
import com.sa45team7.lussis.rest.model.Disbursement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisbursementsFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView disbursementListView;

    public DisbursementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disbursements, container, false);

        refreshLayout = view.findViewById(R.id.disbursement_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
            }
        });

        disbursementListView = view.findViewById(R.id.disbursement_list);


        return view;
    }

    private void getList() {
        Call<List<Disbursement>> call = LUSSISClient.getApiService().getDisbursements();
        call.enqueue(new Callback<List<Disbursement>>() {
            @Override
            public void onResponse(Call<List<Disbursement>> call, Response<List<Disbursement>> response) {

            }

            @Override
            public void onFailure(Call<List<Disbursement>> call, Throwable t) {

            }
        });
    }

    private void checkListEmpty() {
        boolean isEmpty = disbursementListView.getAdapter().getItemCount() == 0;
        disbursementListView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}
