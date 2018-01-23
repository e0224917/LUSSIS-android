package com.sa45team7.lussis.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.adapters.PendingReqAdapter;
import com.sa45team7.lussis.fragments.PendingReqFragment;
import com.sa45team7.lussis.rest.LUSSISClient;
import com.sa45team7.lussis.rest.model.Requisition;
import com.sa45team7.lussis.rest.model.Stationery;
import com.sa45team7.lussis.utils.ErrorUtil;
import com.sa45team7.lussis.utils.InternetConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationeryDetail extends AppCompatActivity {

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationery_detail);

        Intent i=getIntent();
        Display (i.getStringExtra("itemNum"));
    }

    private void Display(final String itemNum) {
        Call<Stationery> call= LUSSISClient.getApiService().getStationery(itemNum);
        call.enqueue((new Callback<Stationery>() {
            @Override
            public void onResponse(Call<Stationery> call, Response<Stationery> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ((EditText) findViewById(R.id.itemNum)).setText(itemNum);
                    ((EditText) findViewById(R.id.itemDesc)).setText(response.body().Description);
                    ((EditText) findViewById(R.id.itemBin)).setText(response.body().BinNum);
                    ((EditText) findViewById(R.id.itemQty)).setText(response.body().AvailableQty);
                    Spinner sCategory=(Spinner) findViewById(R.id.itemCategory);
                    sCategory.setSelection(getIndex(sCategory,response.body().Category));
                    Spinner sUnitOfMeasure=(Spinner) findViewById(R.id.itemCategory);
                    sUnitOfMeasure.setSelection(getIndex(sUnitOfMeasure,response.body().getUnitOfMeasure()));
                }else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Stationery> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        }));
        ((EditText) findViewById(R.id.itemNum)).setText(itemNum);
    }

    //helper for spinner
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

}
