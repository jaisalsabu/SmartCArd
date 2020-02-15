package com.example.smartcard.ui.notifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartcard.MainActivity;
import com.example.smartcard.Qr;
import com.example.smartcard.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment {
TextView txt,classs,div,year,email,phone,pphone;
ImageView img,qr;
Bitmap bitmap;
    SharedPreferences sh;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
         sh= Objects.requireNonNull(getActivity()).getSharedPreferences("data",MODE_PRIVATE);
txt=root.findViewById(R.id.nme);
classs=root.findViewById(R.id.classs);
div=root.findViewById(R.id.dep);
email=root.findViewById(R.id.email);
phone=root.findViewById(R.id.phone);
pphone=root.findViewById(R.id.pphone);
img=root.findViewById(R.id.img);
txt.setText(sh.getString("name",null));
classs.setText(sh.getString("class",null));
div.setText(sh.getString("department",null));
email.setText(sh.getString("email",null));
phone.setText(sh.getString("phone",null));
pphone.setText(sh.getString("pphone",null));
       Picasso.get().load(sh.getString("image",null)).into(img);

       /* try {
            bitmap = TextToImageEncode("hi");
        } catch (WriterException e) {
            e.printStackTrace();
        }
        img.setImageBitmap(bitmap);*/
           /* String path = saveImage(bitmap);  //give read write permission
            Toast.makeText(getContext(), "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();*/

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

 @Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


inflater.inflate(R.menu.studreg,menu);
super.onCreateOptionsMenu(menu, inflater);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
int id=item.getItemId();



if (id== R.id.studreg){

startActivity(new Intent(getActivity(), Qr.class).putExtra("qr",sh.getString("sid",null)));


}




return super.onOptionsItemSelected(item);
}


}