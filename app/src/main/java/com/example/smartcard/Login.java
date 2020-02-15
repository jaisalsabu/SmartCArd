package com.example.smartcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
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

public class Login extends AppCompatActivity {
EditText email,pass;
ImageButton img;
CheckBox ch;
Button sign2,kl;
TextView forgot;
String name,bal,classs,div,dep,sid,emaill,phone,pphone,pro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        img=findViewById(R.id.signin);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        sign2=findViewById(R.id.signup2);
        kl=findViewById(R.id.kl);
        forgot=findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Forgotpass.class));
            }
        });
        kl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Merchant.class));
            }
        });
        sign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Adminlogin.class));
            }
        });
        ch=findViewById(R.id.checkbox);
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ch.setText("Hide Password");
                }
                else
                {

                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ch.setText("Show Password");
                }
            }
        });

    }
    public void signin(final View view)
    {
        if(email.getText().toString().isEmpty()||pass.getText().toString().isEmpty())
        {
            Snackbar.make(view,"Fill All Fields",Snackbar.LENGTH_LONG).show();
        }
        else
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://anoopsuvarnan1.000webhostapp.com/smartcard/login.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//If we are getting success from server
                           // Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_obj = jsonArray.getJSONObject(i);
                                    name=json_obj.getString("name");
                                    bal=json_obj.getString("walletbalance");
sid=json_obj.getString("sid");
//ba = json_obj.getString("balance");

classs=json_obj.getString("class");
dep=json_obj.getString("Department");
emaill=json_obj.getString("email");
phone=json_obj.getString("phone");
pphone=json_obj.getString("p_number");
pro=json_obj.getString("image");
                                }
//Toast.makeText(Recharge.this, "your new balnce is "+ba, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(response.contains("success"))
                            {
                                startActivity(new Intent(Login.this,MainActivity.class));
                                SharedPreferences sh=getSharedPreferences("data",MODE_PRIVATE);
                                SharedPreferences.Editor e=sh.edit();
                                e.putString("name",name);
                                e.putString("bal",bal);
                                e.putString("sid",sid);
                                e.putString("class",classs);
                                e.putString("department",dep);
                                e.putString("email",emaill);
                                e.putString("phone",phone);
                                e.putString("pphone",pphone);
                                e.putString("image",pro);
                                e.apply();
                            }
                            else
                            {
                                Snackbar.make(view,"Invalid Email Or Password",Snackbar.LENGTH_LONG).show();
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

                    params.put("name",email.getText().toString());
                    params.put("password",pass.getText().toString() );
// Toast.makeText(MainActivity.this,"submitted",Toast.LENGTH_LONG).show();

//returning parameter
                    return params;
                }

            };

// m = Integer.parseInt(ba) - Integer.parseInt(result.getContents());
// balance.setText(m+"");


//Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
            requestQueue.add(stringRequest);
        }



    }

        }

