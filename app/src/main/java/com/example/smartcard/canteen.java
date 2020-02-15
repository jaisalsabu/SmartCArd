package com.example.smartcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class canteen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] sex = {"LUNCH", "SNACKS", "BREAKFAST","BEAVERAGES"};
    Spinner gender;
    TextView txt1;
    EditText edther;
    Button scanid, payamt;
    IntentIntegrator qrscan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen);
        gender = findViewById(R.id.gender);
        edther = findViewById(R.id.bc_dob);
        gender.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sex);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scanid = findViewById(R.id.button7);
        payamt = findViewById(R.id.button6);
        txt1 = findViewById(R.id.textView11);
        gender.setAdapter(aa);
        qrscan = new IntentIntegrator(this);
        scanid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrscan.initiateScan();
            }
        });
        payamt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://anoopsuvarnan1.000webhostapp.com/smartcard/merchantget.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Success")) {
                                    Toast.makeText(canteen.this, "Payment Recived", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(canteen.this, "Success", Toast.LENGTH_LONG).show();
                                }

//If we are getting success from server
                                //Toast.makeText(Payment.this,response,Toast.LENGTH_LONG).show();

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

                        params.put("sid", txt1.getText().toString());
                        params.put("amount", edther.getText().toString());
                        params.put("type","Debit");
                        params.put("item",gender.getSelectedItem().toString());
                        params.put("shop","Canteen");

// Toast.makeText(MainActivity.this,"submitted",Toast.LENGTH_LONG).show();

//returning parameter
                        return params;
                    }

                };

// m = Integer.parseInt(ba) - Integer.parseInt(result.getContents());
// balance.setText(m+"");


//Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(canteen.this);
                requestQueue.add(stringRequest);
            }

        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Toast.makeText(canteen.this,decrypt(result.getContents(),"mylove"),Toast.LENGTH_LONG).show();
                    txt1.setText(decrypt(result.getContents(),"mylove"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //txt1.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String decrypt(String out, String anoop) throws Exception {
        SecretKeySpec k = gen(anoop);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, k);
        byte[] dv = Base64.decode(out, Base64.DEFAULT);
        byte[] deco = cipher.doFinal(dv);
        String n = new String(deco);
        return n;

    }

    private SecretKeySpec gen(String pass) throws Exception {

        final MessageDigest di = MessageDigest.getInstance("SHA-256");

        byte[] bytes = pass.getBytes("UTF-8");
        di.update(bytes, 0, bytes.length);
        byte[] key = di.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "SHA");
        return secretKeySpec;
    }
}
