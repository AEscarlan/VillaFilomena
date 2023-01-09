package com.example.villafilomena.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Frontdesk.Guest_details_model;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager_PaymentHistory_Dashboard extends AppCompatActivity {
    String IP;
    Context context = this;

    RecyclerView paymenthstry_list;

    ArrayList<Guest_details_model> guestholder;
    Manager_PaymentHstry_Adapter.ClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_payment_history);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        IP = preferences.getString("IP_Address", "").trim();

        paymenthstry_list = findViewById(R.id.paymenthstry_list);

        retrieveGuest_Info();
        OnItemClick();
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

                        int listNo_CheckIn = 1;
                        guestholder = new ArrayList<>();

                        if (success.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Guest_details_model model = new Guest_details_model(listNo_CheckIn++, object.getString("booking_id"),object.getString("currentBooking_Date"),object.getString("fullname"),object.getString("checkIn_date"), object.getString("checkIn_time"),
                                        object.getString("checkOut_date"),object.getString("checkOut_time"),object.getString("guest_count"),object.getString("room_id"),object.getString("cottage_id"), object.getString("total_cost"),object.getString("pay"),
                                        object.getString("payment_status"),object.getString("balance"),object.getString("reference_num"),object.getString("booking_status"),object.getString("invoice"));
                                guestholder.add(model);
                            }
                            Manager_PaymentHstry_Adapter adapter = new Manager_PaymentHstry_Adapter(guestholder, clickListener);
                            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                            paymenthstry_list.setLayoutManager(layoutManager);
                            paymenthstry_list.setAdapter(adapter);

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

    private void OnItemClick(){
        clickListener = new Manager_PaymentHstry_Adapter.ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Guest_details_model model = guestholder.get(position);
                //Toast.makeText(Manager_PaymentHistory_Dashboard.this, model.getInvoice(), Toast.LENGTH_SHORT).show();

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.pdf_viewer);

                Button viewpdf = dialog.findViewById(R.id.viewpdf);
                Button downloadpdf = dialog.findViewById(R.id.downloadpdf);

                viewpdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(model.getInvoice()), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                });

                downloadpdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Download the PDF
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(model.getInvoice()));
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        request.setTitle("Invoices");
                        request.setDescription("Downloading...");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "mypdf.pdf");

                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

            }
        };
    }
}