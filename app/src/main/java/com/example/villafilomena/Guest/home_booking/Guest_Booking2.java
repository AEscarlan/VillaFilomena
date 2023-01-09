package com.example.villafilomena.Guest.home_booking;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Guest_Booking2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Guest_Booking2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Guest_Booking2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Guest_Booking2.
     */
    // TODO: Rename and change types and number of parameters
    public static Guest_Booking2 newInstance(String param1, String param2) {
        Guest_Booking2 fragment = new Guest_Booking2();
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

    public static TextView txtCheckInOut,txtKidAdult;
    public static ArrayList<RoomInfos_model> roominfo_holder;
    RecyclerView RoomInfo_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.guest_booking2, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        IP = preferences.getString("IP_Address", "").trim();

        txtCheckInOut = view.findViewById(R.id.guestBooking2_txtchekcIn_Out);
        txtKidAdult = view.findViewById(R.id.guestBooking2_txtKid_Adult);
        RoomInfo_list = view.findViewById(R.id.guestBooking2_RoomView);

        txtCheckInOut.setText(Guest_Booking.txtCheckInOut.getText().toString());
        txtKidAdult.setText(Guest_Booking.txtKidAdult.getText().toString());
        roominfo_holder = new ArrayList<>();

        if(!Guest_Booking.visiblePositions_room.isEmpty()){
            for (int i = 0; i<Guest_Booking.visiblePositions_room.size(); i++){
                RoomInfos(Guest_Booking.visiblePositions_room.get(i));
            }
        }

        MainFrame.Booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFrame.Booknow.setVisibility(View.GONE);
                MainFrame.Done.setVisibility(View.VISIBLE);
                replace(new Guest_Booking3());
            }
        });

        return view;
    }

    private void replace(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame,fragment).commit();
    }

    private void RoomInfos(String id){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/retrieve_Room_Details2.php";

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
                                RoomInfos_model model = new RoomInfos_model(object.getString("id"), object.getString("imageUrl"), object.getString("name"), object.getString("room_capacity"), object.getString("room_rate"));
                                roominfo_holder.add(model);
                            }

                            RoomInfos_adapter2 adapter = new RoomInfos_adapter2(roominfo_holder);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            RoomInfo_list.setLayoutManager(layoutManager);
                            RoomInfo_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(getActivity(), "Failed to get", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
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
                    map.put("id",id);
                    return map;
                }
            };
            myrequest.add(stringRequest);
        }
    }
}