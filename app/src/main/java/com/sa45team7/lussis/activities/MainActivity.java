package com.sa45team7.lussis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sa45team7.lussis.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button scanButton = findViewById(R.id.scan_activity);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanQRActivity.class);
                startActivity(intent);
            }
        });

        Button generateButton = findViewById(R.id.generate_activity);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerateQRActivity.class);
                intent.putExtra("disbursement_id", "1234");
                startActivity(intent);
            }
        });

        Button loginButton = findViewById(R.id.login_activity);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Button manageItemsButton = findViewById(R.id.manage_items_activity);
        manageItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageItemsActivity.class);
                startActivity(intent);
            }
        });
    }
}
