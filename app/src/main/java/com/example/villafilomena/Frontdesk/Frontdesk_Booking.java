package com.example.villafilomena.Frontdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Guest.home_booking.RoomInfos_adapter;
import com.example.villafilomena.Guest.home_booking.RoomInfos_model;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringJoiner;

public class Frontdesk_Booking extends AppCompatActivity {
    String IP;
    Context context = this;
    ArrayList<RoomInfos_model> roominfo_holder;
    ArrayList<String> visiblePositions;

    LinearLayout datePicker;
    EditText guestName, Date, Email, Contact, checkIn_Date, checkIn_Time, checkOut_Date, checkOut_Time, roomPrice, cottagePrice, Total, Statpayment, paymentBalanced, reference;
    TextView roomType,cottageType, guestQty,roomQty, cottageQty, Gcash, Cash, txtReference;
    Button bookNow, cancel, guest_done;
    RadioButton _50percent, _100percent;

    TextView kidDec, kidQty, kidInc, adultDec, adultQty, adultInc;
    int kidqty = 0, adultqty = 0;

    String[] checkInOut_year = new String[2];
    String[] checkInOut_month = new String[2];
    String[] checkInOut_day = new String[2];
    String[] checkInTime = new String[1];
    String[] checkOutTime = new String[1];

    public static int numDays = 0, numNights = 0;

    String paymentStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontdesk_booking);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        IP = preferences.getString("IP_Address", "").trim();

        guestName = findViewById(R.id.walkInBooking_guestName);
        Date = findViewById(R.id.walkInBooking_date);
        Email = findViewById(R.id.walkInBooking_email);
        Contact = findViewById(R.id.walkInBooking_contact);
        datePicker = findViewById(R.id.walkInBooking_DatePicker);
        checkIn_Date = findViewById(R.id.walkInBooking_checkIn_Date);
        checkIn_Time = findViewById(R.id.walkInBooking_checkIn_Time);
        checkOut_Date = findViewById(R.id.walkInBooking_checkOut_Date);
        checkOut_Time = findViewById(R.id.walkInBooking_checkOut_Time);
        guestQty = findViewById(R.id.walkInBooking_guestQty);
        roomQty = findViewById(R.id.walkInBooking_roomQty);
        roomType = findViewById(R.id.walkInBooking_roomType);
        roomPrice = findViewById(R.id.walkInBooking_roomPrice);
        cottageQty = findViewById(R.id.walkInBooking_cottageQty);
        cottageType = findViewById(R.id.walkInBooking_cottageType);
        cottagePrice = findViewById(R.id.walkInBooking_cottagePrice);
        Gcash = findViewById(R.id.walkInBooking_Gcash);
        Cash = findViewById(R.id.walkInBooking_Cash);
        Total = findViewById(R.id.walkInBooking_Total);
        Statpayment = findViewById(R.id.walkInBooking_startPayment);
        _50percent = findViewById(R.id.walkInBooking_50percent);
        _100percent = findViewById(R.id.walkInBooking_100percent);
        paymentBalanced = findViewById(R.id.walkInBooking_paymentBalanced);
        txtReference = findViewById(R.id.walkInBooking_txtreference);
        reference = findViewById(R.id.walkInBooking_reference);
        bookNow = findViewById(R.id.walkInBooking_bookNow);
        cancel = findViewById(R.id.walkInBooking_cancel);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.guest_calendar);

                CalendarView checkIn = dialog.findViewById(R.id.guestCalendar_Checkin);
                CalendarView checkOut = dialog.findViewById(R.id.guestCalendar_Checkout);
                RadioButton CheckIn_Daytour = dialog.findViewById(R.id.guestCalendar_CheckIn_DaytourTime);
                RadioButton CheckIn_Nighttour = dialog.findViewById(R.id.guestCalendar_CheckIn_NighttourTime);
                RadioButton CheckOut_Daytour = dialog.findViewById(R.id.guestCalendar_CheckOut_DaytourTime);
                RadioButton CheckOut_Nighttour = dialog.findViewById(R.id.guestCalendar_CheckOut_NighttourTime);
                Button calendar_ok = dialog.findViewById(R.id.calendar_ok);

                Calendar starDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();

                checkIn.setMinDate(System.currentTimeMillis() - 1000);
                checkOut.setMinDate(System.currentTimeMillis() - 1000);

                checkIn.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        checkInOut_year[0] = String.valueOf(year);
                        checkInOut_month[0] = String.valueOf(month+1);
                        checkInOut_day[0] = String.valueOf(dayOfMonth);
                        starDate.set(year, month, dayOfMonth);
                    }
                });
                checkOut.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        checkInOut_year[1] = String.valueOf(year);
                        checkInOut_month[1] = String.valueOf(month+1);
                        checkInOut_day[1] = String.valueOf(dayOfMonth);
                        endDate.set(year,month, dayOfMonth);
                    }
                });

                calendar_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(CheckIn_Daytour.isChecked()){
                            checkInTime[0] = "Day Tour";
                        }else if(CheckIn_Nighttour.isChecked()){
                            checkInTime[0] = "Night Tour";
                        }

                        if(CheckOut_Daytour.isChecked()){
                            checkOutTime[0] = "Day Tour";
                        }else if(CheckOut_Nighttour.isChecked()){
                            checkOutTime[0] = "Night Tour";
                        }

                        checkIn_Date.setText("Check-In "+checkInOut_day[0]+"/"+checkInOut_month[0]+"/"+checkInOut_year[0]);
                        checkIn_Time.setText(checkInTime[0]);
                        checkOut_Date.setText("Check-Out "+checkInOut_day[1]+"/"+checkInOut_month[1]+"/"+checkInOut_year[1]);
                        checkOut_Time.setText(checkOutTime[0]);

                        long difference = endDate.getTimeInMillis() - starDate.getTimeInMillis();

                        numDays = (int) (difference / 86400000);
                        numNights = (int) (difference / 86400000);

                        //check_RoomSched(checkInOut_day[0]+"/"+checkInOut_month[0]+"/"+checkInOut_year[0], checkInOut_day[1]+"/"+checkInOut_month[1]+"/"+checkInOut_year[1], checkInTime[0], checkOutTime[0]);

                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });

        guestQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.guest_quantity);

                kidDec = dialog.findViewById(R.id.kidDec);
                kidQty = dialog.findViewById(R.id.kidQty);
                kidInc = dialog.findViewById(R.id.kidInc);
                adultDec = dialog.findViewById(R.id.adultDec);
                adultQty = dialog.findViewById(R.id.adultQty);
                adultInc = dialog.findViewById(R.id.adultInc);
                guest_done = dialog.findViewById(R.id.guest_done);

                kidDec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(kidqty != 0){
                            kidqty--;
                            kidQty.setText(""+kidqty);
                        }
                    }
                });
                kidInc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kidqty++;
                        kidQty.setText(""+kidqty);
                    }
                });

                adultDec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(kidqty != 0){
                            adultqty--;
                            adultQty.setText(""+adultqty);
                        }
                    }
                });
                adultInc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adultqty++;
                        adultQty.setText(""+adultqty);
                    }
                });

                guest_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guestQty.setText(kidqty+" Kid - "+adultqty+" Adult");
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });

        roomType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickRoom(checkInOut_day[0]+"/"+checkInOut_month[0]+"/"+checkInOut_year[0], checkInOut_day[1]+"/"+checkInOut_month[1]+"/"+checkInOut_year[1], checkInTime[0], checkOutTime[0]);
            }
        });

        Gcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.setVisibility(View.VISIBLE);
                txtReference.setVisibility(View.VISIBLE);
            }
        });
        Cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.setVisibility(View.GONE);
                txtReference.setVisibility(View.GONE);
            }
        });

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        _50percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentStatus = "50";
            }
        });
        _100percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentStatus = "100";
            }
        });
    }

    private void pickRoom(String checkIn_Date, String checkOut_Date, String checkIn_Time, String checkOut_Time){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.room_cottage_picker);

        RecyclerView picker = dialog.findViewById(R.id.room_cottage_picker);
        Button done = dialog.findViewById(R.id.RoomCottagePicker_Done);
        Button cancel = dialog.findViewById(R.id.RoomCottagePicker_Cancel);

        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/retrieve_roomSched.php";
            RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        roominfo_holder = new ArrayList<>();
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            RoomInfos_model model = new RoomInfos_model(object.getString("id"), object.getString("imageUrl"), object.getString("name"), object.getString("room_capacity"), object.getString("room_rate"));
                            roominfo_holder.add(model);
                        }

                        RoomInfos_adapter adapter = new RoomInfos_adapter(roominfo_holder);
                        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
                        picker.setLayoutManager(layoutManager);
                        picker.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Failed to get room schedules", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected HashMap<String,String> getParams() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("checkIn_Date",checkIn_Date);
                    map.put("checkOut_Date",checkOut_Date);

                    return map;
                }
            };
            myrequest.add(stringRequest);
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringJoiner room = new StringJoiner("/");
                visiblePositions = new ArrayList<>();
                int childCount = picker.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childView = picker.getLayoutManager().findViewByPosition(i);
                    CardView getCheck = (CardView) childView.findViewById(R.id.roomInfo_Check);
                    if (getCheck.getVisibility() == View.VISIBLE) {
                        final RoomInfos_model model = roominfo_holder.get(i);
                        visiblePositions.add(model.getId());

                        room.add(model.getName());
                    }
                }

                roomType.setText(""+room);
                roomQty.setText(""+visiblePositions.size());
                dialog.hide();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        dialog.show();
    }


}