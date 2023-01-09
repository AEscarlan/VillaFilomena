package com.example.villafilomena.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.IP_Address;
import com.example.villafilomena.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Booking_Dashboard extends AppCompatActivity {
    String IP;
    final Context context = this;
    private static final int PICK_ROOM_IMAGE_REQUEST = 1, PICK_COTTAGE_IMAGE_REQUEST = 2;
    StorageReference RoomImageReference, CottageImageReference;
    Dialog dialog;
    Uri imageUri;
    ImageView edit, save, FeeDetails, addRoom, addCottage, RoomImage, CottageImage;
    EditText RoomName, RoomCapacity, RoomRate, CottageName, CottageCapacity, CottageRate;
    ProgressBar Roomprogress, Cottageprogress;

    //popup_feedetails
    TextView txtDaytour, txtDaytourKidAge, txtDaytourKidFee, txtDaytourAdultAge, txtDaytourAdultFee,
            txtNighttour, txtNighttourKidAge, txtNighttourKidFee, txtNighttourAdultAge, txtNighttourAdultFee;
    TextView DaytourTime, NighttourTime;
    LinearLayout linear1, linear2, linear3, linear4;
    Button Next, Back;
    ImageView feeDet_Save,Daytour_KidAgeFee_edit,Daytour_AdultAgeFee_edit,Nighttour_KidAgeFee_edit,Nighttour_AdultAgeFee_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_dashboard);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        IP = preferences.getString("IP_Address", "").trim();

        RoomImageReference = FirebaseStorage.getInstance().getReference("RoomImages");
        CottageImageReference = FirebaseStorage.getInstance().getReference("CottageImages");

        edit = findViewById(R.id.managerBooking_edit);
        save = findViewById(R.id.managerBooking_save);
        FeeDetails = findViewById(R.id.manager_FeeDetails);
        addRoom = findViewById(R.id.booking_addRoom);
        addCottage = findViewById(R.id.booking_addCottage);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                addRoom.setVisibility(View.VISIBLE);
                addCottage.setVisibility(View.VISIBLE);
                FeeDetails.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
                addRoom.setVisibility(View.GONE);
                addCottage.setVisibility(View.GONE);
                FeeDetails.setVisibility(View.GONE);
            }
        });

        FeeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeeDetail();
            }
        });

        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRoomImage();
            }
        });

        addCottage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCottageImage();
            }
        });
    }

    private void FeeDetail(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_feedetails);

        txtDaytour = dialog.findViewById(R.id.popup_booking_txtDaytour);
        feeDet_Save = dialog.findViewById(R.id.popup_FeeDet_Save);
        DaytourTime = dialog.findViewById(R.id.popup_booking_DayTourTime);
        linear1 = dialog.findViewById(R.id.popup_booking_lnrlyt1);
        txtDaytourKidAge = dialog.findViewById(R.id.popup_booking_txtDaytourKidAge);
        txtDaytourKidFee = dialog.findViewById(R.id.popup_booking_txtDaytourKidFee);
        Daytour_KidAgeFee_edit = dialog.findViewById(R.id.Daytour_kidAgeFee_Edit);
        linear2 = dialog.findViewById(R.id.popup_booking_lnrlyt2);
        txtDaytourAdultAge = dialog.findViewById(R.id.popup_booking_txtDaytourAdultAge);
        txtDaytourAdultFee = dialog.findViewById(R.id.popup_booking_txtDaytourAdultFee);
        Daytour_AdultAgeFee_edit = dialog.findViewById(R.id.Daytour_AdultAgeFee_Edit);
        txtNighttour = dialog.findViewById(R.id.popup_booking_txtNighttour);
        NighttourTime = dialog.findViewById(R.id.popup_booking_NightTourTime);
        linear3 = dialog.findViewById(R.id.popup_booking_lnrlyt3);
        txtNighttourKidAge = dialog.findViewById(R.id.popup_booking_txtNighttourKidAge);
        txtNighttourKidFee = dialog.findViewById(R.id.popup_booking_txtNighttourKidFee);
        Nighttour_KidAgeFee_edit = dialog.findViewById(R.id.Nighttour_kidAgeFee_Edit);
        linear4 = dialog.findViewById(R.id.popup_booking_lnrlyt4);
        txtNighttourAdultAge = dialog.findViewById(R.id.popup_booking_txtNighttourAdultAge);
        txtNighttourAdultFee = dialog.findViewById(R.id.popup_booking_txtNighttourAdultFee);
        Nighttour_AdultAgeFee_edit = dialog.findViewById(R.id.Nighttour_AdultAgeFee_Edit);
        Next = dialog.findViewById(R.id.popup_booking_btnNext);
        Back = dialog.findViewById(R.id.popup_booking_btnBack);

        final String[] DayTime = new String[2];
        final String[] NightTime = new String[2];

        ArrayList<String> kidAge = new ArrayList<>();
        ArrayList<String> AdultAge = new ArrayList<>();

        final String[] min_kidAge = new String[2];
        final String[] max_kidAge = new String[2];
        final String[] min_AdultAge = new String[2];
        final String[] max_AdultAge = new String[2];

        feeDet_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntranceFee_Details();
                dialog.hide();
            }
        });

        DaytourTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog1 = new Dialog(context);
                dialog1.setContentView(R.layout.popup_time_picker);

                TimePicker timePicker_From = dialog1.findViewById(R.id.popup_timePick_From);
                TimePicker timePicker_To = dialog1.findViewById(R.id.popup_timePick_To);
                Button Done = dialog1.findViewById(R.id.popup_timePick_Done);
                Button To = dialog1.findViewById(R.id.timePick_TO);
                Button From = dialog1.findViewById(R.id.timePick_FROM);

                timePicker_From.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";

                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10){
                            min = "0" + minutes ;
                        }
                        else {
                            min = String.valueOf(minutes);
                        }

                        DayTime[0] = hour+":"+min+" "+timeSet;
                    }
                });

                timePicker_To.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";

                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10){
                            min = "0" + minutes ;
                        }
                        else {
                            min = String.valueOf(minutes);
                        }

                        DayTime[1] = hour+":"+min+" "+timeSet;
                    }
                });

                To.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        From.setVisibility(View.VISIBLE);
                        To.setVisibility(View.GONE);
                        Done.setVisibility(View.VISIBLE);
                        timePicker_To.setVisibility(View.VISIBLE);
                        timePicker_From.setVisibility(View.GONE);
                    }
                });

                From.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        From.setVisibility(View.GONE);
                        To.setVisibility(View.VISIBLE);
                        Done.setVisibility(View.GONE);
                        timePicker_To.setVisibility(View.GONE);
                        timePicker_From.setVisibility(View.VISIBLE);
                    }
                });

                Done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DaytourTime.setText(DayTime[0]+" - "+DayTime[1]);

                        dialog1.hide();
                    }
                });

                dialog1.show();
            }
        });

        NighttourTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog1 = new Dialog(context);
                dialog1.setContentView(R.layout.popup_time_picker);

                TimePicker timePicker_From = dialog1.findViewById(R.id.popup_timePick_From);
                TimePicker timePicker_To = dialog1.findViewById(R.id.popup_timePick_To);
                Button Done = dialog1.findViewById(R.id.popup_timePick_Done);
                Button To = dialog1.findViewById(R.id.timePick_TO);
                Button From = dialog1.findViewById(R.id.timePick_FROM);

                timePicker_From.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";

                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10){
                            min = "0" + minutes ;
                        }
                        else {
                            min = String.valueOf(minutes);
                        }

                        NightTime[0] = hour+":"+min+" "+timeSet;
                    }
                });

                timePicker_To.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";

                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10){
                            min = "0" + minutes ;
                        }
                        else {
                            min = String.valueOf(minutes);
                        }

                        NightTime[1] = hour+":"+min+" "+timeSet;
                    }
                });

                To.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        From.setVisibility(View.VISIBLE);
                        To.setVisibility(View.GONE);
                        Done.setVisibility(View.VISIBLE);
                        timePicker_To.setVisibility(View.VISIBLE);
                        timePicker_From.setVisibility(View.GONE);
                    }
                });

                From.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        From.setVisibility(View.GONE);
                        To.setVisibility(View.VISIBLE);
                        Done.setVisibility(View.GONE);
                        timePicker_To.setVisibility(View.GONE);
                        timePicker_From.setVisibility(View.VISIBLE);
                    }
                });

                Done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NighttourTime.setText(NightTime[0]+" - "+NightTime[1]);

                        dialog1.hide();
                    }
                });

                dialog1.show();
            }
        });

        Daytour_KidAgeFee_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=1; i<18; i++){
                    kidAge.add(String.valueOf(i));
                }

                Dialog dialog1 = new Dialog(dialog.getContext());
                dialog1.setContentView(R.layout.popup_age_fee);

                //Entrance Fee Details for Kids at Day
                LinearLayout Kid_DayFee = dialog1.findViewById(R.id.popup_Kid_DayFee);
                LinearLayout Adult_DayFee= dialog1.findViewById(R.id.popup_Adult_DayFee);
                LinearLayout Kid_NightFee = dialog1.findViewById(R.id.popup_Kid_NightFee);
                LinearLayout Adult_NightFee = dialog1.findViewById(R.id.popup_Adult_NightFee);
                Spinner DayKidAge_Min = dialog1.findViewById(R.id.popup_kidAge_Min_Day);
                Spinner DayKidAge_Max = dialog1.findViewById(R.id.popup_kidAge_Max_Day);
                EditText KidFeeDay = dialog1.findViewById(R.id.popup_Kidfee_Day);
                Button Done = dialog1.findViewById(R.id.popup_Agefee_Done);

                Kid_DayFee.setVisibility(View.VISIBLE);
                Adult_DayFee.setVisibility(View.GONE);
                Kid_NightFee.setVisibility(View.GONE);
                Adult_NightFee.setVisibility(View.GONE);

                ArrayAdapter adapter = new ArrayAdapter(dialog.getContext(), R.layout.spinner_age, kidAge);
                DayKidAge_Min.setAdapter(adapter);
                DayKidAge_Max.setAdapter(adapter);

                Done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        min_kidAge[0] = DayKidAge_Min.getSelectedItem().toString();
                        max_kidAge[0] = DayKidAge_Max.getSelectedItem().toString();

                        txtDaytourKidAge.setText("("+min_kidAge[0] + " - " + max_kidAge[0]+" years old)");
                        txtDaytourKidFee.setText(KidFeeDay.getText().toString());
                        dialog1.hide();
                    }
                });

                dialog1.show();
            }
        });

        Daytour_AdultAgeFee_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdultAge.add("and above");
                for (int i = Integer.parseInt(max_kidAge[0]); i<100; i++){
                    AdultAge.add(String.valueOf(i));
                }

                Dialog dialog1 = new Dialog(dialog.getContext());
                dialog1.setContentView(R.layout.popup_age_fee);

                //Entrance Fee Details for Adult at Day
                LinearLayout Kid_DayFee = dialog1.findViewById(R.id.popup_Kid_DayFee);
                LinearLayout Adult_DayFee= dialog1.findViewById(R.id.popup_Adult_DayFee);
                LinearLayout Kid_NightFee = dialog1.findViewById(R.id.popup_Kid_NightFee);
                LinearLayout Adult_NightFee = dialog1.findViewById(R.id.popup_Adult_NightFee);
                Spinner DayAdultAge_Min = dialog1.findViewById(R.id.popup_AdultAge_Min_Day);
                Spinner DayAdultAge_Max = dialog1.findViewById(R.id.popup_AdultAge_Max_Day);
                EditText AdultFeeDay = dialog1.findViewById(R.id.popup_Adultfee_Day);
                Button Done = dialog1.findViewById(R.id.popup_Agefee_Done);

                Kid_DayFee.setVisibility(View.GONE);
                Adult_DayFee.setVisibility(View.VISIBLE);
                Kid_NightFee.setVisibility(View.GONE);
                Adult_NightFee.setVisibility(View.GONE);

                ArrayAdapter adapter = new ArrayAdapter(dialog.getContext(), R.layout.spinner_age, AdultAge);
                DayAdultAge_Min.setAdapter(adapter);
                DayAdultAge_Max.setAdapter(adapter);

                Done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        min_AdultAge[0] = DayAdultAge_Min.getSelectedItem().toString();
                        max_AdultAge[0] = DayAdultAge_Max.getSelectedItem().toString();

                        txtDaytourAdultAge.setText("("+min_AdultAge[0] + " - " + max_AdultAge[0]+" years old)");
                        txtDaytourAdultFee.setText(AdultFeeDay.getText().toString());
                        dialog1.hide();
                    }
                });

                dialog1.show();
            }
        });

        Nighttour_KidAgeFee_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=1; i<18; i++){
                    kidAge.add(String.valueOf(i));
                }

                Dialog dialog1 = new Dialog(dialog.getContext());
                dialog1.setContentView(R.layout.popup_age_fee);

                //Entrance Fee Details for Kids at Night
                LinearLayout Kid_DayFee = dialog1.findViewById(R.id.popup_Kid_DayFee);
                LinearLayout Adult_DayFee= dialog1.findViewById(R.id.popup_Adult_DayFee);
                LinearLayout Kid_NightFee = dialog1.findViewById(R.id.popup_Kid_NightFee);
                LinearLayout Adult_NightFee = dialog1.findViewById(R.id.popup_Adult_NightFee);
                Spinner NightKidAge_Min = dialog1.findViewById(R.id.popup_KidAge_Min_Night);
                Spinner NightKidAge_Max = dialog1.findViewById(R.id.popup_KidAge_Max_Night);
                EditText KidFeeNight = dialog1.findViewById(R.id.popup_Kidfee_Night);
                Button Done = dialog1.findViewById(R.id.popup_Agefee_Done);

                Kid_DayFee.setVisibility(View.GONE);
                Adult_DayFee.setVisibility(View.GONE);
                Kid_NightFee.setVisibility(View.VISIBLE);
                Adult_NightFee.setVisibility(View.GONE);

                ArrayAdapter adapter = new ArrayAdapter(dialog.getContext(), R.layout.spinner_age, kidAge);
                NightKidAge_Min.setAdapter(adapter);
                NightKidAge_Max.setAdapter(adapter);

                Done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        min_kidAge[1] = NightKidAge_Min.getSelectedItem().toString();
                        max_kidAge[1] = NightKidAge_Max.getSelectedItem().toString();

                        txtNighttourKidAge.setText("("+min_kidAge[1] + " - " + max_kidAge[1]+" years old)");
                        txtNighttourKidFee.setText(KidFeeNight.getText().toString());

                        dialog1.hide();
                    }
                });

              dialog1.show();
            }
        });

        Nighttour_AdultAgeFee_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdultAge.add("and above");
                for (int i = Integer.parseInt(max_kidAge[1]); i<100; i++){
                    AdultAge.add(String.valueOf(i));
                }

                Dialog dialog1 = new Dialog(dialog.getContext());
                dialog1.setContentView(R.layout.popup_age_fee);

                //Entrance Fee Details for Adult at Night
                LinearLayout Kid_DayFee = dialog1.findViewById(R.id.popup_Kid_DayFee);
                LinearLayout Adult_DayFee= dialog1.findViewById(R.id.popup_Adult_DayFee);
                LinearLayout Kid_NightFee = dialog1.findViewById(R.id.popup_Kid_NightFee);
                LinearLayout Adult_NightFee = dialog1.findViewById(R.id.popup_Adult_NightFee);
                Spinner NightAdultAge_Min = dialog1.findViewById(R.id.popup_AdultAge_Min_Night);
                Spinner NightAdultAge_Max = dialog1.findViewById(R.id.popup_AdultAge_Max_Night);
                EditText AdultFeeNight = dialog1.findViewById(R.id.popup_Adultfee_Night);
                Button Done = dialog1.findViewById(R.id.popup_Agefee_Done);

                Kid_DayFee.setVisibility(View.GONE);
                Adult_DayFee.setVisibility(View.GONE);
                Kid_NightFee.setVisibility(View.GONE);
                Adult_NightFee.setVisibility(View.VISIBLE);

                ArrayAdapter adapter = new ArrayAdapter(dialog.getContext(), R.layout.spinner_age, AdultAge);
                NightAdultAge_Min.setAdapter(adapter);
                NightAdultAge_Max.setAdapter(adapter);

                Done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        min_AdultAge[1] = NightAdultAge_Min.getSelectedItem().toString();
                        max_AdultAge[1] = NightAdultAge_Max.getSelectedItem().toString();

                        txtNighttourAdultAge.setText("("+min_AdultAge[1] + " - " + max_AdultAge[1]+" years old)");
                        txtNighttourAdultFee.setText(AdultFeeNight.getText().toString());

                        dialog1.hide();
                    }
                });

                dialog1.show();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDaytour.setVisibility(View.GONE);
                DaytourTime.setVisibility(View.GONE);
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.GONE);
                Next.setVisibility(View.GONE);
                txtNighttour.setVisibility(View.VISIBLE);
                NighttourTime.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.VISIBLE);
                linear4.setVisibility(View.VISIBLE);
                Back.setVisibility(View.VISIBLE);
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDaytour.setVisibility(View.VISIBLE);
                DaytourTime.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.VISIBLE);
                Next.setVisibility(View.VISIBLE);
                txtNighttour.setVisibility(View.GONE);
                NighttourTime.setVisibility(View.GONE);
                linear3.setVisibility(View.GONE);
                linear4.setVisibility(View.GONE);
                Back.setVisibility(View.GONE);
            }
        });

        dialog.show();
    }

    private void EntranceFee_Details(){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+ IP+"/VillaFilomena/entranceFee_Details.php";
            RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Success")){
                        Toast.makeText(context, "Insert Successful", Toast.LENGTH_SHORT).show();
                    }
                    else if(response.equals("Failed")){
                        Toast.makeText(context, "Insert Failed", Toast.LENGTH_SHORT).show();
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
                    map.put("daytour_time",DaytourTime.getText().toString());
                    map.put("daytour_kidAge",txtDaytourKidAge.getText().toString());
                    map.put("daytour_kidFee",txtDaytourKidFee.getText().toString());
                    map.put("daytour_adultAge",txtDaytourAdultAge.getText().toString());
                    map.put("daytour_adultFee",txtDaytourAdultFee.getText().toString());
                    map.put("nighttour_time",NighttourTime.getText().toString());
                    map.put("nighttour_kidAge",txtNighttourKidAge.getText().toString());
                    map.put("nighttour_kidFee",txtNighttourKidFee.getText().toString());
                    map.put("nighttour_adultAge",txtNighttourAdultAge.getText().toString());
                    map.put("nighttour_adultFee",txtNighttourAdultFee.getText().toString());
                    return map;
                }
            };
            myrequest.add(stringRequest);
        }
    }

    private void chooseRoomImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_ROOM_IMAGE_REQUEST);
    }

    private void chooseCottageImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_COTTAGE_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_ROOM_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.popup_roomdetails);

            RoomName = dialog.findViewById(R.id.popup_RoomName);
            RoomImage = dialog.findViewById(R.id.popup_RoomImage);
            RoomCapacity = dialog.findViewById(R.id.popup_RoomCapacity);
            RoomRate = dialog.findViewById(R.id.popup_RoomRate);
            Button Done = dialog.findViewById(R.id.popup_RoomDetail_Done);
            Roomprogress = dialog.findViewById(R.id.popup_Room_progressbar);

            RoomImage.setImageURI(imageUri);

            Done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UploadRoomImage();
                }
            });
            dialog.show();
        }
        else if (requestCode == PICK_COTTAGE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.popup_cottagedetails);

            CottageName = dialog.findViewById(R.id.popup_CottageName);
            CottageImage = dialog.findViewById(R.id.popup_CottageImage);
            CottageCapacity = dialog.findViewById(R.id.popup_CottageCapacity);
            CottageRate = dialog.findViewById(R.id.popup_CottageRate);
            Button Done = dialog.findViewById(R.id.popup_CottageDetail_Done);
            Cottageprogress = dialog.findViewById(R.id.popup_Cottage_progressbar);

            CottageImage.setImageURI(imageUri);

            Done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UploadCottageImage();
                }
            });
            dialog.show();
        }
    }

    private String getfileExt(Uri MyUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(MyUri));
    }

    private void UploadRoomImage(){
        Roomprogress.setVisibility(View.VISIBLE);
        if (imageUri != null){
            StorageReference reference = RoomImageReference.child(RoomName.getText().toString()+"."+getfileExt(imageUri));

            reference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Roomprogress.setVisibility(View.INVISIBLE);
                            dialog.hide();

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    if(!IP.equalsIgnoreCase("")){
                                        String url = "http://"+IP+"/VillaFilomena/room_details.php";
                                        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("Success")){
                                                    Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show();
                                                }
                                                else if(response.equals("Failed")){
                                                    Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show();
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
                                                map.put("imageUrl",imageUrl);
                                                map.put("name",RoomName.getText().toString());
                                                map.put("room_capacity",RoomCapacity.getText().toString());
                                                map.put("room_rate", RoomRate.getText().toString());
                                                return map;
                                            }
                                        };
                                        myrequest.add(stringRequest);
                                    }

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadCottageImage(){
        //imageprogressBar.setVisibility(View.VISIBLE);
        if (imageUri != null){
            StorageReference reference = CottageImageReference.child(System.currentTimeMillis()+"."+getfileExt(imageUri));

            reference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //imageprogressBar.setVisibility(View.INVISIBLE);
                            dialog.hide();

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    String url = "http://"+IP+"/VillaFilomena/cottage_details.php";
                                    RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("Upload Success")){
                                                Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(response.equals("Upload Failed")){
                                                Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show();
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
                                            map.put("imageUrl",imageUrl);
                                            map.put("name",CottageName.getText().toString());
                                            map.put("cottage_capacity",CottageCapacity.getText().toString());
                                            map.put("cottage_rate",CottageRate.getText().toString());
                                            return map;
                                        }
                                    };
                                    myrequest.add(stringRequest);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}