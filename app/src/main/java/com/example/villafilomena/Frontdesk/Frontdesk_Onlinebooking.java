package com.example.villafilomena.Frontdesk;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.FcmNotificationsSender;
import com.example.villafilomena.Guest.home_booking.RoomInfos_model;
import com.example.villafilomena.IP_Address;
import com.example.villafilomena.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringJoiner;

public class Frontdesk_Onlinebooking extends AppCompatActivity {
    String IP;
    Context context = this;
    EditText current_date, guest_name, guest_email, guest_contact, checkIn_DATE, checIn_TIME, checkOut_DATE, checkOut_TIME, guest_qty, room_qty, room_type, room_price, cottage_qty, cottage_type, cottage_price, payment_balance, paymentStat, total, referenceNum;
    Button booknow;

    String booking_id, checkIn_date, checkIn_time, checkOut_date, checkOut_time, cottage_id, total_cost, pay, balance, reference_num;

    Bitmap bmp;

    private static final int PERMISSION_REQUEST_CODE = 200;

    public static ArrayList<RoomInfos_model> roominfo_holder;

    StorageReference InvoiceReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontdesk_onlinebooking);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        IP = preferences.getString("IP_Address", "").trim();

        roominfo_holder = new ArrayList<>();

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.villa_filomena_logo);
        InvoiceReference = FirebaseStorage.getInstance().getReference("Invoice");

        if (!checkPermission()) {
            requestPermission();
        }

        current_date = findViewById(R.id.FD_onlineBook_currentDate);
        guest_name = findViewById(R.id.FD_onlineBook_Gname);
        guest_email = findViewById(R.id.FD_onlineBook_Gemail);
        guest_contact = findViewById(R.id.FD_onlineBook_Gnumber);
        checkIn_DATE = findViewById(R.id.FD_onlineBook_CheckInDate);
        checIn_TIME = findViewById(R.id.FD_onlineBook_CheckInTime);
        checkOut_DATE = findViewById(R.id.FD_onlineBook_CheckOutDate);
        checkOut_TIME = findViewById(R.id.FD_onlineBook_CheckOutTime);
        guest_qty = findViewById(R.id.FD_onlineBook_GuestQty);
        room_qty = findViewById(R.id.FD_onlineBook_RoomQty);
        room_type = findViewById(R.id.FD_onlineBook_RoomType);
        room_price = findViewById(R.id.FD_onlineBook_RoomPrice);
        cottage_qty = findViewById(R.id.FD_onlineBook_CottageQty);
        cottage_type = findViewById(R.id.FD_onlineBook_CottageType);
        cottage_price = findViewById(R.id.FD_onlineBook_CottagePrice);
        paymentStat = findViewById(R.id.FD_onlineBook_StatPayment);
        payment_balance = findViewById(R.id.FD_onlineBook_Balance);
        total = findViewById(R.id.FD_onlineBook_Total);
        referenceNum = findViewById(R.id.FD_onlineBook_Reference);
        booknow = findViewById(R.id.Frontdesk_onlineBook_btnBooknow);

        guest_name.setText(Frontdesk_Booked.fullname);
        guest_email.setText(Frontdesk_Booked.email);
        guest_contact.setText(Frontdesk_Booked.contactNum);

        room_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringJoiner str = new StringJoiner("\n");
                for(int i=0; i<roominfo_holder.size(); i++){
                    RoomInfos_model model = roominfo_holder.get(i);
                    str.add(model.getName());
                }

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.confirmation_roomtype_dialog);

                TextView roomList = dialog.findViewById(R.id.confirmationDialog_roomList);

                roomList.setText(""+str);

                dialog.show();

            }
        });

        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGuestInformation();
                insert_RoomSched();
                generatePDF();
               /* FcmNotificationsSender notificationsSender = new FcmNotificationsSender(Frontdesk_Booked.token, "Booking Update", "Your Booking is Confirmed", getApplicationContext(), Frontdesk_Onlinebooking.this);
                notificationsSender.SendNotifications();
                referenceNum.setText(Frontdesk_Booked.token);*/
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getGuestInformation();
    }

    private void generatePDF(){
        //1 inch = 72 points so 1 * 72
        int width = 612;
        int height = 792;

        PdfDocument document = new PdfDocument();

        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Matrix matrix = new Matrix();
        float scaleFactor = Math.min((float) 120 / bmp.getWidth(), (float) 120 / bmp.getHeight());
        bmp.getHeight();
        matrix.setScale(scaleFactor, scaleFactor);
        matrix.postTranslate(10, 25);

        canvas.drawBitmap(bmp, matrix, paint);

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(16f);
        canvas.drawText("Villa Filomena Natural Spring Resort", 130, 80, titlePaint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(12f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Purok 2 Kaytimbog, Indang, Cavite", 130, 100, paint);
        canvas.drawText("09391136357", 130, 115, paint);

        Date InvoiceDate = new Date();
        DateFormat InvoiceDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(12f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Invoice Date: "+InvoiceDateFormat.format(InvoiceDate), width-20, 80, paint);
        canvas.drawText("Online Booking", width-20, 100, paint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(20f);
        canvas.drawText("Invoice", width/2, 160, titlePaint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Guest Name: "+Frontdesk_Booked.fullname, 50, 200, paint);
        canvas.drawText("No. of People", 50, 220, paint);
        canvas.drawText("Check-in: "+checkIn_date+" - "+checkIn_time, 50, 240, paint);
        canvas.drawText("Check-out: "+checkOut_date+" - "+checkOut_time, 50, 260, paint);

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(14f);
        canvas.drawText("Description", 50, 320, titlePaint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(14f);
        canvas.drawText("Quantity", width/2, 320, titlePaint);

        titlePaint.setTextAlign(Paint.Align.RIGHT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(14f);
        canvas.drawText("Price", width-50, 320, titlePaint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Room Details", 50, 340, paint);
        canvas.drawText("Cottage Details", 50, 360, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(14f);
        paint.setColor(Color.BLACK);
        canvas.drawText("0", width/2, 340, paint);
        canvas.drawText("0", width/2, 360, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(14f);
        paint.setColor(Color.BLACK);
        canvas.drawText("0", width-50, 340, paint);
        canvas.drawText("0", width-50, 360, paint);

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(16f);
        canvas.drawText("Total Amount: "+total_cost, 50, 440, titlePaint);
        canvas.drawText("Payment Method: GCash", 50, 460, titlePaint);
        canvas.drawText("Reference Number: "+reference_num, 50, 480, titlePaint);
        canvas.drawText("Paid: "+ pay, 50, 500, titlePaint);
        canvas.drawText("Balance: "+balance, 50, 520, titlePaint);

        document.finishPage(page);

        String folder_name = "Invoice";
        File f = new File(Environment.getExternalStorageDirectory(), folder_name);
        if (!f.exists()) {
            f.mkdirs();
        }

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

        String InvoiceName = Frontdesk_Booked.fullname+"_"+dateFormat.format(date);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+folder_name;

        File file = new File(path, InvoiceName+".pdf");

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(getApplicationContext(), "Booking is confirmed", Toast.LENGTH_SHORT).show();

            Uri invoice_Name = Uri.fromFile(new File(path + "/" + file.getName().toString()));

            StorageReference reference = InvoiceReference.child(file.getName().toString());
            reference.putFile(invoice_Name)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(Frontdesk_Onlinebooking.this, "uploaded in firebase", Toast.LENGTH_SHORT).show();

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String invoiceUrl = uri.toString();

                                    Confirm_Booking(invoiceUrl);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Frontdesk_Onlinebooking.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
    }

    private void getGuestInformation(){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/retrieve_bookingInfos.php";

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

                                current_date.setText(object.getString("currentBooking_Date"));
                                checkIn_DATE.setText(object.getString("checkIn_date"));
                                checIn_TIME.setText(object.getString("checkIn_time"));
                                checkOut_DATE.setText(object.getString("checkOut_date"));
                                checkOut_TIME.setText(object.getString("checkOut_time"));
                                guest_qty.setText(object.getString("guest_count"));
                                payment_balance.setText(object.getString("balance"));
                                paymentStat.setText(object.getString("payment_status"));
                                referenceNum.setText(object.getString("reference_num"));
                                total.setText(object.getString("total_cost"));

                                String[] roomID = object.getString("room_id").split(",");
                                for (String s : roomID) {
                                    RoomInfos(s.trim());
                                }
                                String roomSize = String.valueOf(roomID.length);
                                room_qty.setText(roomSize);

                                checkIn_date = object.getString("checkIn_date");
                                checkIn_time = object.getString("checkIn_time");
                                checkOut_date = object.getString("checkOut_date");
                                checkOut_time = object.getString("checkOut_time");

                                booking_id = object.getString("booking_id");
                                cottage_id = object.getString("cottage_id");

                                pay = object.getString("pay");
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Failed to get", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Failed to get guest informations", Toast.LENGTH_SHORT).show();
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
                    map.put("email", Frontdesk_Booked.email);

                    return map;
                }
            };
            myrequest.add(stringRequest);
        }
    }

    private void RoomInfos(String id){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/retrieve_Room_Details2.php";

            RequestQueue myrequest = Volley.newRequestQueue(context);
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

                            double roomPrice = 0;
                            for(int i=0; i<roominfo_holder.size(); i++){
                                RoomInfos_model model = roominfo_holder.get(i);
                                roomPrice += Double.parseDouble(model.getRoom_rate());
                            }

                            room_price.setText(""+roomPrice);

                        }else{
                            Toast.makeText(context, "Failed to get", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(context, "Failed to get room infos", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context,error.getMessage().toString(), Toast.LENGTH_LONG).show();
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

    private void Confirm_Booking(String invoiceUrl){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/bookingConfirmation.php";

            RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Success")){
                        Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show();

                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(Frontdesk_Booked.token, "Front Desk", "Your Booking is Confirmed", getApplicationContext(), Frontdesk_Onlinebooking.this);
                        notificationsSender.SendNotifications();
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
                    map.put("booking_id",booking_id);
                    map.put("booking_status","Confirmed");
                    map.put("invoice",invoiceUrl);
                    return map;
                }
            };
            myrequest.add(stringRequest);
        }
    }

    private void insert_RoomSched(){
        if(!IP.equalsIgnoreCase("")){
            for(int i=0; i<roominfo_holder.size(); i++){
                RoomInfos_model model = roominfo_holder.get(i);

                String url = "http://"+IP+"/VillaFilomena/insert_roomSched.php";
                RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Success")){
                            Toast.makeText(getApplicationContext(), "Room "+model.getName()+" Schedule is set", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.equals("Failed")){
                            Toast.makeText(getApplicationContext(), "Unexpected Error, Please try again! ", Toast.LENGTH_SHORT).show();
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
                        map.put("room_id",model.getId());
                        map.put("start_Date",checkIn_date);
                        map.put("end_Date",checkOut_date);
                        map.put("start_Time",checkIn_time);
                        map.put("end_Time",checkOut_time);
                        map.put("bookedBy",Frontdesk_Booked.email);

                        return map;
                    }
                };
                myrequest.add(stringRequest);
            }
        }
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}