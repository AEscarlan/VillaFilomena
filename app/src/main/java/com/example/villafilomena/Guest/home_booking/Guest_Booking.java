package com.example.villafilomena.Guest.home_booking;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Guest_Booking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Guest_Booking extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Guest_Booking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frontdesk_Booking.
     */
    // TODO: Rename and change types and number of parameters
    public static Guest_Booking newInstance(String param1, String param2) {
        Guest_Booking fragment = new Guest_Booking();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    String IP;

    LinearLayout checkIn_checkOut, adult_child;
    Button guest_done;
    public static TextView txtCheckInOut,txtKidAdult,kidDec, kidQty, kidInc, adultDec, adultQty, adultInc;
    public static int kidqty = 0, adultqty = 0;
    //entrance fee details
    TextView daytourTime, nightTourTime, daytourFee, nighttourFee;

    RecyclerView RoomInfo_list, CottageInfo_list;
    ArrayList<RoomInfos_model> roominfo_holder;
    ArrayList<CottageInfos_model> cottageinfo_holder;

    public static ArrayList<String> visiblePositions_room;
    public static ArrayList<String> visiblePositions_cottage;
    public static int numDays = 0;
    public static int numNights = 0;
    public static double kidFee_Day, kidFee_Night, adultFee_Day, adultFee_Night;

    public static final String[] checkInOut_year = new String[2];
    public static final String[] checkInOut_month = new String[2];
    public static final String[] checkInOut_day = new String[2];
    public static final String[] checkIn_Time = new String[1];
    public static final String[] checkOut_Time = new String[1];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.guest_booking, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        IP = preferences.getString("IP_Address", "").trim();

        txtCheckInOut = view.findViewById(R.id.guestBooking_txtchekcIn_Out);
        txtKidAdult = view.findViewById(R.id.guestBooking_txtKid_Adult);
        checkIn_checkOut = view.findViewById(R.id.checkIn_checkOut);
        adult_child = view.findViewById(R.id.adult_child);
        daytourTime = view.findViewById(R.id.guestBooking_daytour_Time);
        nightTourTime = view.findViewById(R.id.guestBooking_nighttour_Time);
        daytourFee = view.findViewById(R.id.guestBooking_daytour_Fee);
        nighttourFee = view.findViewById(R.id.guestBooking_nighttour_Fee);
        RoomInfo_list = view.findViewById(R.id.guestBooking_RoomInfos_List);
        CottageInfo_list = view.findViewById(R.id.guestBooking_CottageInfos_List);

        EntranceFee_Details();
        Room_Infos();
        Cottage_Infos();

        checkIn_checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), Calendar.class));

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater1 = getActivity().getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.guest_calendar, null);

                CalendarView checkIn = view1.findViewById(R.id.guestCalendar_Checkin);
                CalendarView checkOut = view1.findViewById(R.id.guestCalendar_Checkout);
                RadioButton CheckIn_Daytour = view1.findViewById(R.id.guestCalendar_CheckIn_DaytourTime);
                RadioButton CheckIn_Nighttour = view1.findViewById(R.id.guestCalendar_CheckIn_NighttourTime);
                RadioButton CheckOut_Daytour = view1.findViewById(R.id.guestCalendar_CheckOut_DaytourTime);
                RadioButton CheckOut_Nighttour = view1.findViewById(R.id.guestCalendar_CheckOut_NighttourTime);
                Button calendar_ok = view1.findViewById(R.id.calendar_ok);

                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                dialog.show();

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
                            checkIn_Time[0] = "Day Tour";
                        }else if(CheckIn_Nighttour.isChecked()){
                            checkIn_Time[0] = "Night Tour";
                        }

                        if(CheckOut_Daytour.isChecked()){
                            checkOut_Time[0] = "Day Tour";
                        }else if(CheckOut_Nighttour.isChecked()){
                            checkOut_Time[0] = "Night Tour";
                        }

                        txtCheckInOut.setText("Check-In "+checkInOut_day[0]+"/"+checkInOut_month[0]+"/"+checkInOut_year[0]+" - "+ checkIn_Time[0] +
                                "\n"+"Check-Out "+checkInOut_day[1]+"/"+checkInOut_month[1]+"/"+checkInOut_year[1]+" - "+ checkOut_Time[0]);

                        long difference = endDate.getTimeInMillis() - starDate.getTimeInMillis();

                        numDays = (int) (difference / 86400000);
                        numNights = (int) (difference / 86400000);

                        check_RoomSched(checkInOut_day[0]+"/"+checkInOut_month[0]+"/"+checkInOut_year[0], checkInOut_day[1]+"/"+checkInOut_month[1]+"/"+checkInOut_year[1], checkIn_Time[0], checkOut_Time[0]);

                        dialog.hide();
                    }
                });
            }
        });

        adult_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater1 = getActivity().getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.guest_quantity, null);

                kidDec = view1.findViewById(R.id.kidDec);
                kidQty = view1.findViewById(R.id.kidQty);
                kidInc = view1.findViewById(R.id.kidInc);
                adultDec = view1.findViewById(R.id.adultDec);
                adultQty = view1.findViewById(R.id.adultQty);
                adultInc = view1.findViewById(R.id.adultInc);
                guest_done = view1.findViewById(R.id.guest_done);

                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                dialog.show();

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
                        txtKidAdult.setText(kidqty+" Kid - "+adultqty+" Adult");
                        dialog.hide();
                    }
                });
            }
        });

        MainFrame.Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFrame.Continue.setVisibility(View.GONE);
                MainFrame.Booknow.setVisibility(View.VISIBLE);
                visiblePositions_room = new ArrayList<>();
                //RoomInfos_adapter adapter = (RoomInfos_adapter) RoomInfo_list.getAdapter();

                int childCount = RoomInfo_list.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childView = RoomInfo_list.getLayoutManager().findViewByPosition(i);
                    CardView getCheck = (CardView) childView.findViewById(R.id.roomInfo_Check);
                    if (getCheck.getVisibility() == View.VISIBLE) {
                        final RoomInfos_model model = roominfo_holder.get(i);
                        visiblePositions_room.add(model.getId());
                    }
                }

                visiblePositions_cottage = new ArrayList<>();
                int cottageChild_Count = CottageInfo_list.getChildCount();
                for (int i = 0; i < cottageChild_Count; i++){
                    View childView = CottageInfo_list.getLayoutManager().findViewByPosition(i);
                    CardView getCheck = (CardView) childView.findViewById(R.id.roomInfo_Check);
                    if (getCheck.getVisibility() == View.VISIBLE){
                        final CottageInfos_model model = cottageinfo_holder.get(i);
                        visiblePositions_cottage.add(model.getId());
                    }
                }
                replace(new Guest_Booking2());
            }
        });

        return view;
    }

    private void replace(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame,fragment).commit();
    }

    private void EntranceFee_Details(){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/retrieve_EntranceFee_Details.php";

            RequestQueue myrequest = Volley.newRequestQueue(getActivity());
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

                                String day_tour = object.getString("day_tour");
                                String daytour_kid_age = object.getString("daytour_kid_age");
                                String daytour_kid_fee = object.getString("daytour_kid_fee");
                                String daytour_adult_age = object.getString("daytour_adult_age");
                                String daytour_adult_fee = object.getString("daytour_adult_fee");
                                String night_tour = object.getString("night_tour");
                                String nighttour_kid_age = object.getString("nighttour_kid_age");
                                String nighttour_kid_fee = object.getString("nighttour_kid_fee");
                                String nighttour_adult_age = object.getString("nighttour_adult_age");
                                String nighttour_adult_fee = object.getString("nighttour_adult_fee");

                                kidFee_Day = Double.parseDouble(daytour_kid_fee);
                                kidFee_Night = Double.parseDouble(nighttour_kid_fee);
                                adultFee_Day = Double.parseDouble(daytour_adult_fee);
                                adultFee_Night = Double.parseDouble(nighttour_adult_fee);

                                daytourTime.setText(day_tour);
                                daytourFee.setText("KID"+daytour_kid_age+" - "+daytour_kid_fee+"\n"+"ADULT"+daytour_adult_age+" - "+daytour_adult_fee);
                                nightTourTime.setText(night_tour);
                                nighttourFee.setText("KID"+nighttour_kid_age+" - "+nighttour_kid_fee+"\n"+"ADULT"+nighttour_adult_age+" - "+nighttour_adult_fee);

                            }
                        }else{
                            Toast.makeText(getActivity(), "Failed to get", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(getActivity(), "Failed failed to get entrance fee details", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(),error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            myrequest.add(stringRequest);
        }
    }

    private void Room_Infos(){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/retrieve_Room_Details.php";
            RequestQueue myrequest = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if(success.equals("1")){
                            roominfo_holder = new ArrayList<>();

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                RoomInfos_model model = new RoomInfos_model(object.getString("id"), object.getString("imageUrl"), object.getString("name"), object.getString("room_capacity"), object.getString("room_rate"));
                                roominfo_holder.add(model);

                            }

                            RoomInfos_adapter adapter = new RoomInfos_adapter(roominfo_holder);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            RoomInfo_list.setLayoutManager(layoutManager);
                            RoomInfo_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(getActivity(), "Failed to get", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(getActivity(), "Failed to get room information", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(),error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            myrequest.add(stringRequest);
        }
    }

    private void check_RoomSched(String checkIn_Date, String checkOut_Date, String checkIn_Time, String checkOut_Time){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/retrieve_roomSched.php";
            RequestQueue myrequest = Volley.newRequestQueue(getActivity());
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
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        RoomInfo_list.setLayoutManager(layoutManager);
                        RoomInfo_list.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }catch (Exception e){
                        Toast.makeText(getActivity(), "Failed to get room schedules", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(),error.getMessage().toString(), Toast.LENGTH_LONG).show();
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
    }

    private void Cottage_Infos(){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/retrieve_Cottage_Details.php";
            RequestQueue myrequest = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if(success.equals("1")){
                            cottageinfo_holder = new ArrayList<>();

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                CottageInfos_model model = new CottageInfos_model(object.getString("id"), object.getString("imageUrl"), object.getString("name"), object.getString("cottage_capacity"), object.getString("cottage_rate"));
                                cottageinfo_holder.add(model);

                            }

                            CottageInfos_adapter adapter = new CottageInfos_adapter(cottageinfo_holder);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            CottageInfo_list.setLayoutManager(layoutManager);
                            CottageInfo_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(getActivity(), "Failed to get", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(getActivity(), "Failed to get room information", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(),error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            myrequest.add(stringRequest);
        }
    }

}