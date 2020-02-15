package com.example.smartcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Qr extends AppCompatActivity {
ImageView img3;
Bitmap bitmap;
Button dqr;
Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        i=getIntent();
        img3=findViewById(R.id.imageView3);
        dqr=findViewById(R.id.dqr);
        try {
            bitmap = TextToImageEncode(encrypt(i.getStringExtra("qr"),"mylove"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        img3.setImageBitmap(bitmap);

        dqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = saveImage(bitmap);  //give read write permission
                Toast.makeText(Qr.this, "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    500, 500, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File sdCard = Environment.getExternalStorageDirectory();
      //  File dir = new File(sdCard.getAbsolutePath() + "/FasTagQR");
        File wallpaperDirectory = new File(sdCard.getAbsolutePath() + "/SmartCardQR");
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }
    private String encrypt(String user, String pass) throws Exception
    {
        SecretKeySpec k=gen(pass);
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,k);
        byte[] en=cipher.doFinal(user.getBytes());
        String vall= Base64.encodeToString(en,Base64.DEFAULT);
        return vall;

    }

    private SecretKeySpec gen(String pass) throws Exception {

        final MessageDigest di= MessageDigest.getInstance("SHA-256");

        byte[] bytes=pass.getBytes("UTF-8");
        di.update(bytes,0,bytes.length);
        byte[] key=di.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,"SHA");
        return secretKeySpec;

    }
   /* private String decrypt(String out, String anoop) throws Exception {
        SecretKeySpec k=gen(anoop);
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,k);
        byte[] dv= Base64.decode(out,Base64.DEFAULT);
        byte[] deco= cipher.doFinal(dv);
        String n=new String(deco);
        return n;

    }

    private SecretKeySpec gen(String pass) throws Exception {

        final MessageDigest di= MessageDigest.getInstance("SHA-256");

        byte[] bytes=pass.getBytes("UTF-8");
        di.update(bytes,0,bytes.length);
        byte[] key=di.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,"SHA");
        return secretKeySpec;

    }*/
}
