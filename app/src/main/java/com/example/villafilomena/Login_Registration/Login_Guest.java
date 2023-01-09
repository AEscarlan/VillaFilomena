package com.example.villafilomena.Login_Registration;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Guest.home_booking.MainFrame;
import com.example.villafilomena.IP_Address;
import com.example.villafilomena.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class Login_Guest extends AppCompatActivity {

    TextView line1, line2;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    Button login, cont_google;
    TextView register;
    TextInputEditText password,email;

    public static final String SHARED_PREFS = "";
    public static String EMAIL = "";
    public static String user_email = "";

    String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_guest);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        IP = preferences.getString("IP_Address", "").trim();

        email = findViewById(R.id.loginGeust_email);
        password = findViewById(R.id.loginGuest_password);
        login = findViewById(R.id.loginGeust_btnLog);
        cont_google = findViewById(R.id.loginGuest_contgoogle);
        register = findViewById(R.id.login_register);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);

        line1.setPaintFlags(line1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        line2.setPaintFlags(line2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IP.equalsIgnoreCase("")){
                    String url = "http://"+IP+"/VillaFilomena/login.php";
                    //Toast.makeText(getApplicationContext(), "Account already exists", Toast.LENGTH_SHORT).show();
                    RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("not_exist")){
                                Toast.makeText(getApplicationContext(), "Account doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                            else if(response.equals("true")){

                                user_email = email.getText().toString();

                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(EMAIL, email.getText().toString());
                                editor.apply();

                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                    return;
                                                }

                                                // Get new FCM registration token
                                                String token = task.getResult();
                                                update_guestToken(token);
                                            }
                                        });

                                startActivity(new Intent(getApplicationContext(), MainFrame.class));
                                finish();
                                //Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_SHORT).show();
                            }
                            else if(response.equals("false")){
                                Toast.makeText(getApplicationContext(),"Incorrect Password", Toast.LENGTH_LONG).show();
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
                            map.put("email",email.getText().toString());
                            map.put("password",password.getText().toString());
                            return map;
                        }
                    };
                    myrequest.add(stringRequest);
                }
            }
        });

        cont_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),Register_Guest.class));
                finish();
            }
        });
    }

    private void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            checkEmail(task);
        }

    }

    public void checkEmail(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            //String email = account.getEmail();
            // Use email to search for user in database

            String url1 = "http://"+IP+"/VillaFilomena/check_email.php";
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("true")){
                        startActivity(new Intent(Login_Guest.this, MainFrame.class));
                    }
                    else if(response.equals("false")){
                        Toast.makeText(getApplicationContext(), "Account doesn't exist", Toast.LENGTH_LONG).show();
                        gsc.signOut();
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
                    map.put("email",account.getEmail());
                    return map;
                }
            };
            myrequest.add(stringRequest);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void update_guestToken(String token) {
        if (!IP.equalsIgnoreCase("")) {
            String url = "http://"+IP+"/VillaFilomena/update_guestToken.php";
            RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Success")){
                        //Toast.makeText(Login_Guest.this, "Token Updated", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Login_Guest.this, "Token update Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected HashMap<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("email", email.getText().toString());
                    map.put("token", token);
                    return map;
                }
            };
            myrequest.add(stringRequest);
        }
    }
}