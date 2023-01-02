package com.example.villafilomena.Frontdesk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Frontdesk_Booked extends AppCompatActivity {

    String IP;

    RecyclerView requestList;

    ArrayList<Frontdesk_userDetailsModel> request_holder;
    Request_Adapter.ClickListener clickListener;

    public static String email, fullname, address, contactNum;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontdesk_booked);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        IP = preferences.getString("IP_Address", "").trim();

        retrieve_BookingInfos();

        MyAsyncTask task = new MyAsyncTask();
        task.execute();

       /* thread = null;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    retrieve_BookingInfos();
                }
            }
        });
        thread.start();*/

        requestList = findViewById(R.id.frontdesk_requestList);
        OnItemClick();

    }

    private class MyAsyncTask extends AsyncTask<Void, Void, ArrayList<Frontdesk_userDetailsModel>> {
        @Override
        protected ArrayList<Frontdesk_userDetailsModel> doInBackground(Void... params) {
            // Perform database query in the background
            //...
            request_holder = new ArrayList<>();

            if(!IP.equalsIgnoreCase("")){
                String url = "http://"+IP+"/VillaFilomena/retrieve_userDetails.php";
                RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if(success.equals("1")){

                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Frontdesk_userDetailsModel model = new Frontdesk_userDetailsModel(object.getString("email"),object.getString("fullname"),object.getString("contact"),object.getString("address"));
                                    request_holder.add(model);
                                }

                            }else{
                                Toast.makeText(getApplicationContext(), "Failed to get", Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                myrequest.add(stringRequest);
            }

            return request_holder;
        }

        @Override
        protected void onPostExecute(ArrayList<Frontdesk_userDetailsModel> request_holder) {
            // Update the UI with the results of the database query
            //...
            Request_Adapter adapter = new Request_Adapter(Frontdesk_Booked.this,request_holder, clickListener);
            GridLayoutManager layoutManager = new GridLayoutManager(Frontdesk_Booked.this, 2);
            requestList.setLayoutManager(layoutManager);
            requestList.setAdapter(adapter);
        }
    }

    private void retrieve_BookingInfos(){

    }

    private void OnItemClick(){
        clickListener = new Request_Adapter.ClickListener() {
            @Override
            public void onClick(View v,int position) {
                Frontdesk_userDetailsModel model = request_holder.get(position);

                startActivity(new Intent(getApplicationContext(), Frontdesk_Onlinebooking.class));

                email = model.getEmail();
                fullname = model.getFullname();
                contactNum = model.getContact();
                address = model.getAddress();
            }
        };
    }
}