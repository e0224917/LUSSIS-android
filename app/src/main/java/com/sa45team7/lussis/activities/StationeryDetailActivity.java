package com.sa45team7.lussis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.data.UserManager;
import com.sa45team7.lussis.dialogs.AdjustDialog;
import com.sa45team7.lussis.rest.LUSSISClient;
import com.sa45team7.lussis.rest.model.Adjustment;
import com.sa45team7.lussis.rest.model.LUSSISResponse;
import com.sa45team7.lussis.rest.model.Stationery;
import com.sa45team7.lussis.utils.ErrorUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sa45team7.lussis.dialogs.AdjustDialog.REQUEST_ADJUST;

public class StationeryDetailActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private TextView uomText;
    private String itemNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationery_detail);

        itemNum = getIntent().getStringExtra("itemNum");

        categorySpinner = findViewById(R.id.itemCategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.stationery_category, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        uomText = findViewById(R.id.itemUnitOfMeasure);

        Button adjustButton = findViewById(R.id.adjustButton);

        adjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdjustDialog dialog = new AdjustDialog();
                dialog.show(getSupportFragmentManager(), "adjust_dialog");
            }
        });

        fetchStationery();
    }

    private void fetchStationery() {
        Call<Stationery> call = LUSSISClient.getApiService().getStationery(itemNum);
        call.enqueue((new Callback<Stationery>() {
            @Override
            public void onResponse(@NonNull Call<Stationery> call, @NonNull Response<Stationery> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ((EditText) findViewById(R.id.itemNum)).setText(itemNum);
                    ((EditText) findViewById(R.id.itemDesc)).setText(response.body().getDescription());
                    ((EditText) findViewById(R.id.itemBin)).setText(response.body().getBinNum());

                    String qty = String.valueOf(response.body().getAvailableQty());
                    ((EditText) findViewById(R.id.itemQty)).setText(qty);

                    categorySpinner.setSelection(getIndex(categorySpinner, response.body().category));
                    categorySpinner.setEnabled(false);

                    uomText.setText(response.body().getUnitOfMeasure());

                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Stationery> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
        ((EditText) findViewById(R.id.itemNum)).setText(itemNum);
    }

    private void adjustStock(Adjustment adjustment) {
        Call<LUSSISResponse> call = LUSSISClient.getApiService().stockAdjust(adjustment);
        call.enqueue(new Callback<LUSSISResponse>() {
            @Override
            public void onResponse(Call<LUSSISResponse> call, Response<LUSSISResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(StationeryDetailActivity.this,
                            response.body().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    String error = ErrorUtil.parseError(response).getMessage();
                    Toast.makeText(StationeryDetailActivity.this,
                            error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LUSSISResponse> call, Throwable t) {
                Toast.makeText(StationeryDetailActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //helper for spinner
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADJUST && resultCode == RESULT_OK) {
            Adjustment adjustment = new Adjustment();
            adjustment.setItemNum(itemNum);
            adjustment.setQuantity(data.getIntExtra("number", 0));
            adjustment.setReason(data.getStringExtra("reason"));
            adjustment.setRequestEmpNum(UserManager.getInstance().getCurrentEmployee().getEmpNum());
            adjustStock(adjustment);
        }
    }

}
