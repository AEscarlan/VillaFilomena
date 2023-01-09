package com.example.villafilomena.Frontdesk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.villafilomena.R;

public class Frontdesk_Dashboard extends AppCompatActivity {

    CardView booking, onlineBooking, calendar, expArrival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontdesk_dashboard);

        booking = findViewById(R.id.frontdesk_booking);
        onlineBooking = findViewById(R.id.frontdesk_OnlineBooking);
        expArrival = findViewById(R.id.frontdesk_ExpArrival);
        calendar = findViewById(R.id.frontdesk_calendar);

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Frontdesk_Booking.class));
            }
        });
        onlineBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Frontdesk_Booked.class));
            }
        });
        expArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Frontdesk_ExpArrival_Dashboard.class));
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Frontdesk_Calendar.class));
            }
        });
    }
}