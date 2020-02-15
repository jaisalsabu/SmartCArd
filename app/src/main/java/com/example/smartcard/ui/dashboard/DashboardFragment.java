package com.example.smartcard.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcard.Login;
import com.example.smartcard.MainActivity;
import com.example.smartcard.Payment;
import com.example.smartcard.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import safety.com.br.android_shake_detector.core.ShakeCallback;
import safety.com.br.android_shake_detector.core.ShakeDetector;
import safety.com.br.android_shake_detector.core.ShakeOptions;

import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    EditText sid, ramt;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private ShakeDetector shakeDetector;
    private long mShakeTimestamp;
    private int mShakeCount;
    FloatingActionButton ftb;
    TextView txt;
    SharedPreferences sh;
    String amn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        sh = Objects.requireNonNull(getActivity()).getSharedPreferences("data", MODE_PRIVATE);
        sid = root.findViewById(R.id.editText);
        txt = root.findViewById(R.id.textView2);
        txt.setText(sh.getString("bal", null));
        ramt = root.findViewById(R.id.editText2);
        ftb = root.findViewById(R.id.floatingActionButton);
        ftb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sid.getText().toString().isEmpty() || ramt.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Fill All Fields", Snackbar.LENGTH_LONG).show();
                } else {
                    new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Recharge Smartcard")
                            .setConfirmText("Recharge")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog
                                            .setTitleText("Recharge Initiated")
                                            .setContentText("Please Choose Payment Method")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    startActivity(new Intent(getActivity(), Payment.class).putExtra("aa", ramt.getText().toString()).putExtra("sid",sid.getText().toString()));
                                }
                            })
                            .show();
                }
            }
        });
        // buildView();

        ShakeOptions options = new ShakeOptions()
                .background(true)
                .interval(1000)
                .shakeCount(2)
                .sensibility(2.0f);

        this.shakeDetector = new ShakeDetector(options).start(getContext(), new ShakeCallback() {
            @Override
            public void onShake() {
                Log.d("event", "onShake");
                Toast.makeText(getActivity(),"shake",Toast.LENGTH_LONG).show();
                recharge();
            }
        });

        return root;
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        shakeDetector.destroy(getActivity());
        super.onDestroy();
    }
public void recharge()
{
    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://anoopsuvarnan1.000webhostapp.com/smartcard/fetchbalance.php",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

//If we are getting success from server
                  //  Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json_obj = jsonArray.getJSONObject(i);

amn=json_obj.getString("walletbalance");
//ba = json_obj.getString("balance");
                            txt.setText(amn);
                            new SweetAlertDialog(getActivity())
                                    .setTitleText("Your Balance amount is:\u20B9"+amn)
                                    .show();

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

            params.put("sid",sh.getString("sid",null));

// Toast.makeText(MainActivity.this,"submitted",Toast.LENGTH_LONG).show();

//returning parameter
            return params;
        }

    };

// m = Integer.parseInt(ba) - Integer.parseInt(result.getContents());
// balance.setText(m+"");


//Adding the string request to the queue
    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
    requestQueue.add(stringRequest);
}
    }


