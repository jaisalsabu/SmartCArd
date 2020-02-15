package com.example.smartcard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Payment extends AppCompatActivity implements PaymentResultListener {
TextView txt,ss;
Intent i;
LottieAnimationView lo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ss=findViewById(R.id.textView3);
        lo=findViewById(R.id.lo);
        startPayment();

    }
    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        i=getIntent();
        Toast.makeText(Payment.this,i.getStringExtra("aa"),Toast.LENGTH_LONG).show();
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "SmartCard");
            options.put("description", "Pay with ease");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(Integer.parseInt(Objects.requireNonNull(i.getStringExtra("aa")))*100));

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        MediaPlayer m=MediaPlayer.create(Payment.this,R.raw.apple);
        m.start();
        //txt.setText(s);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://anoopsuvarnan1.000webhostapp.com/smartcard/amtup.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//If we are getting success from server
                        Toast.makeText(Payment.this,response,Toast.LENGTH_LONG).show();

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_obj = jsonArray.getJSONObject(i);

//ba = json_obj.getString("balance");


                            }
//Toast.makeText(Recharge.this, "your new balnce is "+ba, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//You can handle error here if you want
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//Adding parameters to request

                params.put("sid",i.getStringExtra("sid"));
                params.put("amount",i.getStringExtra("aa"));
// Toast.makeText(MainActivity.this,"submitted",Toast.LENGTH_LONG).show();

//returning parameter
                return params;
            }

        };

// m = Integer.parseInt(ba) - Integer.parseInt(result.getContents());
// balance.setText(m+"");


//Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(Payment.this);
        requestQueue.add(stringRequest);
        push();
    }

    @Override
    public void onPaymentError(int i, String s) {
lo.setVisibility(View.GONE);
ss.setVisibility(View.GONE);
    }
    public void push()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://anoopsuvarnan1.000webhostapp.com/smartcard/push.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//If we are getting success from server

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_obj = jsonArray.getJSONObject(i);


                            }
//Toast.makeText(Recharge.this, "your new balnce is "+ba, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//You can handle error here if you want
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//Adding parameters to request

                params.put("sid",i.getStringExtra("sid"));
                params.put("amount",i.getStringExtra("aa"));
                params.put("type","credit");
                params.put("item","Amount Credited");
                params.put("shop","self");

//returning parameter
                return params;
            }

        };


//Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(Payment.this);
        requestQueue.add(stringRequest);
    }
}
