package com.example.villafilomena.Guest.home_booking;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Guest.Profile.Account;
import com.example.villafilomena.Login_Registration.Login_Guest;
import com.example.villafilomena.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainFrame extends AppCompatActivity {
    String IP;
    final Context context = this;
    ImageView account, bannerView, mainmenu;
    TextView home, book;
    AppBarLayout appbar;
    NestedScrollView nested;
    public static Button Continue, Booknow, Done;

    DrawerLayout drawerLayout;
    ImageView navigationBar,iv_logout;
    NavigationView navigationView;
    CardView profile, bookingHsty, ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_frame);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        IP = preferences.getString("IP_Address", "").trim();

        profile = findViewById(R.id.MainFrame_guestProfile);
        bookingHsty = findViewById(R.id.MainFrame_guestBookings);
        ratings = findViewById(R.id.MainFrame_guestRatings);
        mainmenu = findViewById(R.id.MainMenu);
        bannerView = findViewById(R.id.bannerView);
        home = findViewById(R.id.mainHome);
        book = findViewById(R.id.mainBook);
        appbar = findViewById(R.id.appbar);
        nested = findViewById(R.id.nested);
        Continue = findViewById(R.id.guestBooking_Continue);
        Booknow = findViewById(R.id.guestBooking2_booknow);
        Done = findViewById(R.id.guestBooking3_done);

        home.setPaintFlags(home.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        home.setPaintFlags(home.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        home.setPadding(0,0,0,5);
        replace_home(new Home());


       /* account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Account.class));
            }
        });*/
        mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nested.fullScroll(View.FOCUS_UP);
                toggle(true);
                nested.setNestedScrollingEnabled(true);

                home.setPaintFlags(home.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                home.setPaintFlags(home.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                home.setPadding(0,0,0,5);

                if((book.getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) == Paint.UNDERLINE_TEXT_FLAG &&
                        (book.getPaintFlags() & Paint.FAKE_BOLD_TEXT_FLAG) == Paint.FAKE_BOLD_TEXT_FLAG){
                    book.setPaintFlags(book.getPaintFlags() ^ Paint.UNDERLINE_TEXT_FLAG);
                    book.setPaintFlags(book.getPaintFlags() ^ Paint.FAKE_BOLD_TEXT_FLAG);
                    book.setPadding(0,5,0,0);

                    replace_home(new Home());
                    Continue.setVisibility(View.GONE);
                }
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nested.fullScroll(View.FOCUS_UP);
                toggle(false);
                nested.setNestedScrollingEnabled(false);

                book.setPaintFlags(book.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                book.setPaintFlags(book.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                book.setPadding(0,0,0,5);

                if((home.getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) == Paint.UNDERLINE_TEXT_FLAG &&
                        (home.getPaintFlags() & Paint.FAKE_BOLD_TEXT_FLAG) == Paint.FAKE_BOLD_TEXT_FLAG){
                    home.setPaintFlags(home.getPaintFlags() ^ Paint.UNDERLINE_TEXT_FLAG);
                    home.setPaintFlags(home.getPaintFlags() ^ Paint.FAKE_BOLD_TEXT_FLAG);
                    home.setPadding(0,5,0,0);

                    replace_book(new Guest_Booking());
                    Continue.setVisibility(View.VISIBLE);
                }
            }
        });

        onSetNavigationDrawerEvents();
    }

    private void onSetNavigationDrawerEvents() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView, true);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFrame.this, Account.class));
            }
        });
        bookingHsty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFrame.this, Guest_Booking_Hstry.class));
            }
        });
        ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.rating_dialog);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(layoutParams);

                RatingBar rate = dialog.findViewById(R.id.Guest_Rating);
                EditText comment = dialog.findViewById(R.id.Guest_RatingComment);
                Button submit = dialog.findViewById(R.id.guest_RatesFeedback_submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float rating = rate.getRating();
                        insert_RatesFeedback(String.valueOf(rating), comment.getText().toString());
                        dialog.hide();
                    }
                });

                dialog.show();
            }
        });
    }

    private void insert_RatesFeedback(String rate, String feedback){
        if(!IP.equalsIgnoreCase("")){
            String url = "http://"+IP+"/VillaFilomena/insert_RatesFeedback.php";
            RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("Success")){
                        Toast.makeText(context, "Thank you for your time!", Toast.LENGTH_SHORT).show();
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
                    map.put("user_email", Login_Guest.user_email);
                    map.put("rates", rate);
                    map.put("feedbacks", feedback);
                    return map;
                }
            };
            myrequest.add(stringRequest);
        }
    }

    private void replace_book(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame,fragment).commit();
    }

    private void replace_home(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame,fragment).commit();
    }

    private void toggle(boolean show) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        params.height = bannerView.getHeight();
        appbar.setLayoutParams(params);
        appbar.setExpanded(show);

    }

   /* @Override
    protected void onStart(){
        super.onStart();
        //home.performClick();
        home.setPaintFlags(home.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        home.setPaintFlags(home.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        home.setPadding(0,0,0,5);
        replace_home(new Home());
    }*/
}