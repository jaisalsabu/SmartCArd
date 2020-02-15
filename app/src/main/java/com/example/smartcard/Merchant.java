package com.example.smartcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Merchant extends AppCompatActivity {
EditText u,p;
ImageButton b;
CheckBox c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);
        u=findViewById(R.id.email110);
        p=findViewById(R.id.password110);
        b=findViewById(R.id.signin110);
        c=findViewById(R.id.checkbox110);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    p.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    c.setText("Hide Password");
                }
                else {
                    p.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    c.setText("Show Password");
                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(u.getText().toString().isEmpty()||p.getText().toString().isEmpty())
                {
                    Snackbar.make(v,"Fill All Fields",Snackbar.LENGTH_LONG).show();
                }
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://anoopsuvarnan1.000webhostapp.com/smartcard/merchantlogin.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

//If we are getting success from server
                                    Toast.makeText(Merchant.this, response, Toast.LENGTH_LONG).show();
                                    if (response.equals("success")) {

                                    } else
                                    {
                                        Toast.makeText(Merchant.this,"succesfull",Toast.LENGTH_LONG).show();
                                        Intent iota = new Intent(getApplicationContext(), Merchantpay.class);
                                        startActivity(iota);

                                    }

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

                            params.put("name",u.getText().toString());
                            params.put("password",p.getText().toString());


// Toast.makeText(MainActivity.this,"submitted",Toast.LENGTH_LONG).show();

//returning parameter
                            return params;
                        }

                    };

// m = Integer.parseInt(ba) - Integer.parseInt(result.getContents());
// balance.setText(m+"");


//Adding the string request to the queue
                    RequestQueue requestQueue = Volley.newRequestQueue(Merchant.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}
