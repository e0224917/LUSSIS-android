package com.sa45team7.lussis.ui.mainscreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.rest.LUSSISClient;
import com.sa45team7.lussis.rest.model.Stationery;
import com.sa45team7.lussis.ui.adapters.StationeryAdapter;
import com.sa45team7.lussis.utils.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationeriesFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView stationeryListView;
    private Spinner categorySpinner;
    private SearchView searchView;

    private StationeryAdapter stationeryAdapter;

    public StationeriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stationeries, container, false);

        refreshLayout = view.findViewById(R.id.stationery_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
            }
        });

        stationeryListView = view.findViewById(R.id.stationery_list);
        stationeryAdapter = new StationeryAdapter(new ArrayList<Stationery>());
        stationeryListView.setAdapter(stationeryAdapter);

        categorySpinner = view.findViewById(R.id.category_spinner);
        categorySpinner.setSelection(0);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) categorySpinner.getAdapter().getItem(position);
                stationeryAdapter.getFilter().filter(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                stationeryAdapter.filterByDescription(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                stationeryAdapter.filterByDescription("");
                return false;
            }
        });

        getList();

        return view;
    }

    private void getList() {
        Call<List<Stationery>> call = LUSSISClient.getApiService().getAllStationeries();
        call.enqueue(new Callback<List<Stationery>>() {
            @Override
            public void onResponse(Call<List<Stationery>> call, Response<List<Stationery>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stationeryAdapter = new StationeryAdapter(response.body());
                    stationeryListView.setAdapter(stationeryAdapter);

                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Stationery>> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
