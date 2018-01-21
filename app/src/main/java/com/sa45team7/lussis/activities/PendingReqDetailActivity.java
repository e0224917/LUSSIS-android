package com.sa45team7.lussis.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.sa45team7.lussis.R;
import com.sa45team7.lussis.rest.model.Requisition;

public class PendingReqDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_req_detail);
        String data = getIntent().getStringExtra("requisition");
        Requisition requisition = new Gson().fromJson(data, Requisition.class);


    }
}
