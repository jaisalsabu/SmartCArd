package com.example.smartcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Merchantpay extends AppCompatActivity {
Button canteen,stationery,office;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchantpay);
        canteen=findViewById(R.id.button3);
        stationery=findViewById(R.id.button5);
        office=findViewById(R.id.button4);
        canteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ior =new Intent(getApplicationContext(),canteen.class);
                startActivity(ior);
            }
        });
        stationery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ioste= new Intent(getApplicationContext(),stationery.class);
                startActivity(ioste);
            }
        });
        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iroast=new Intent(getApplicationContext(),store.class);
                startActivity(iroast);
            }
        });
    }
}
