package com.example.villafilomena.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Frontdesk.Guest_arrival_Adapter;
import com.example.villafilomena.Frontdesk.Guest_departure_Adapter;
import com.example.villafilomena.Frontdesk.Guest_details_model;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Manager_SalesReport_Dashboard extends AppCompatActivity {
    String IP;

    ArrayList<Guest_details_model> guestholder;

    TextView pickedDate;
    RecyclerView salesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_sales_report_dashboard);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        IP = preferences.getString("IP_Address", "").trim();

        pickedDate = findViewById(R.id.salesReport_Date);
        salesList = findViewById(R.id.manager_salesList);

    }

    private void retrieveGuest_Info() {
        if (!IP.equalsIgnoreCase("")) {
            String url = "http://" + IP + "/VillaFilomena/retrieve_bookingInfos2.php";
            RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        int listNo_CheckIn = 1, listNo_CheckOut = 1;
                        guestholder = new ArrayList<>();

                        Calendar calendar = Calendar.getInstance();
                        Date currentDate = calendar.getTime();

                        if (success.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String checkIn = object.getString("checkIn_date");
                                String[] checinIn_date = checkIn.split("/");

                                String checkOut = object.getString("checkOut_date");
                                String[] checkOut_date = checkOut.split("/");

                                calendar.set(Calendar.YEAR, Integer.parseInt(checinIn_date[2]));
                                calendar.set(Calendar.MONTH, Integer.parseInt(checinIn_date[1])-1);
                                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(checinIn_date[0]));
                                Date check_In = calendar.getTime();

                                //testing.setText(""+currentDate.toString());

                                int month = calendar.get(Calendar.MONTH);

                                if (month == Integer.parseInt(checinIn_date[1])-1) {
                                    Guest_details_model model = new Guest_details_model(listNo_CheckIn++, object.getString("booking_id"),object.getString("currentBooking_Date"),object.getString("fullname"),object.getString("checkIn_date"), object.getString("checkIn_time"),
                                            object.getString("checkOut_date"),object.getString("checkOut_time"),object.getString("guest_count"),object.getString("room_id"),object.getString("cottage_id"), object.getString("total_cost"),object.getString("pay"),
                                            object.getString("payment_status"),object.getString("balance"),object.getString("reference_num"),object.getString("booking_status"),object.getString("invoice"));
                                    guestholder.add(model);
                                }

                                /*String[] roomID = object.getString("room_id").split(",");
                                for (String s : roomID) {
                                    RoomInfos(s.trim());
                                }
                                String roomSize = String.valueOf(roomID.length);*/
                            }

                            Manager_SalesReport_Adapter adapter = new Manager_SalesReport_Adapter(guestholder);
                            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                            salesList.setLayoutManager(layoutManager);
                            salesList.setAdapter(adapter);

                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to get", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to get guest informations", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected HashMap<String,String> getParams() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("status","Checked_Out");
                    return map;
                }
            };
            myrequest.add(stringRequest);
        }
    }
}