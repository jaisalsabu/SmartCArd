package com.example.smartcard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminHome extends AppCompatActivity {

    EditText name,classs,dep,ema,phone,phonee;
    Button btn,browse;
    private Bitmap bitmap;

    private Uri filePath;
ImageView img;
    private int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        btn=findViewById(R.id.button);
        name=findViewById(R.id.editText10);
        classs=findViewById(R.id.editText11);
        dep=findViewById(R.id.editText12);
        ema=findViewById(R.id.editText13);
        phone=findViewById(R.id.editText14);
        phonee=findViewById(R.id.editText15);
        browse=findViewById(R.id.button2);
        img=findViewById(R.id.imageView2);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()||classs.getText().toString().isEmpty()||dep.getText().toString().isEmpty()||ema.getText().toString().isEmpty()||phone.getText().toString().isEmpty()||phonee.getText().toString().isEmpty())
                {
                    Snackbar s=Snackbar.make(v,"Fill all fields",Snackbar.LENGTH_LONG);
                    s.show();
                }
                else if(phone.getText().toString().length()<10||phonee.getText().toString().length()<10)
                {
                    Snackbar.make(v,"Invalid PhoneNumber",Snackbar.LENGTH_LONG).show();
                }
                else {
up();
                }
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
             img.setImageBitmap(bitmap);
                getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void up()
    {

        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AdminHome.this, "Registering...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if(s.contains("success"))
                {
                    new SweetAlertDialog(AdminHome.this)
                            .setTitleText("Registraion Success")
                            .show();
                }
                else {
                    new SweetAlertDialog(AdminHome.this)
                            .setTitleText("Registraion Failed")
                            .show();
                }
            }

            @SuppressLint("WrongThread")
            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();

                data.put("image", uploadImage);
                data.put("name",name.getText().toString());
                data.put("class",classs.getText().toString());
                data.put("dep",dep.getText().toString());
                data.put("email",ema.getText().toString());
                data.put("phone",phone.getText().toString());
                data.put("pphone",phonee.getText().toString());
                String result = rh.sendPostRequest("https://anoopsuvarnan1.000webhostapp.com/smartcard/addstudent.php", data);

                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
}
