package com.example.villafilomena.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.villafilomena.R;

public class Main_Dashboard extends AppCompatActivity {

    ImageView guesthomepage, roomcottage, frontdesk, paymentHistory, salesReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        guesthomepage = findViewById(R.id.manager_GuestHomePage);
        roomcottage = findViewById(R.id.manager_RoomCottage);
        frontdesk = findViewById(R.id.manager_Frontdesk);
        paymentHistory = findViewById(R.id.manager_PaymentHistory);
        salesReport = findViewById(R.id.manager_salesReport);

        guesthomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main_Dashboard.this, GuestHomePage_Dashboard.class));
            }
        });
        roomcottage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main_Dashboard.this, Booking_Dashboard.class));
            }
        });
        frontdesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main_Dashboard.this, FrontdeskClerks_Dashboard.class));
            }
        });
        paymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Manager_PaymentHistory_Dashboard.class));
            }
        });
        salesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Manager_SalesReport_Dashboard.class));
            }
        });
    }
}