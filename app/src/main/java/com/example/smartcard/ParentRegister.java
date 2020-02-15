package com.example.smartcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ParentRegister extends AppCompatActivity {
EditText name,add,ph,ema,pass,conf;
Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        btn=findViewById(R.id.button);
        name=findViewById(R.id.editText10);
        add=findViewById(R.id.editText11);
        ph=findViewById(R.id.editText12);
        ema=findViewById(R.id.editText13);
        pass=findViewById(R.id.editText14);
        conf=findViewById(R.id.editText15);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()||add.getText().toString().isEmpty()||ph.getText().toString().isEmpty()||ema.getText().toString().isEmpty()||pass.getText().toString().isEmpty()||conf.getText().toString().isEmpty())
                {
                    Snackbar s=Snackbar.make(v,"Fill all fields",Snackbar.LENGTH_LONG);
                    s.show();
                }
                else if(!pass.getText().toString().equals(conf.getText().toString()))
                {
                  Snackbar.make(v,"Password Mismatch",Snackbar.LENGTH_LONG).show();
                }
                else if(ph.getText().toString().length()<10)
                {
                    Snackbar.make(v,"Invalid PhoneNumber",Snackbar.LENGTH_LONG).show();
                }
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://anoopsuvarnan1.000webhostapp.com/smartcard/pRegister.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

//If we are getting success from server
                                   // Toast.makeText(ParentRegister.this,response,Toast.LENGTH_LONG).show();

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

                            params.put("name",name.getText().toString());
                            params.put("add",add.getText().toString());
                            params.put("ph",ph.getText().toString());
                            params.put("email",ema.getText().toString());
                            params.put("pass",pass.getText().toString());

// Toast.makeText(MainActivity.this,"submitted",Toast.LENGTH_LONG).show();

//returning parameter
                            return params;
                        }

                    };

// m = Integer.parseInt(ba) - Integer.parseInt(result.getContents());
// balance.setText(m+"");


//Adding the string request to the queue
                    RequestQueue requestQueue = Volley.newRequestQueue(ParentRegister.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}
