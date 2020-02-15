package com.example.smartcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class Chequeadapter extends RecyclerView.Adapter<Chequeadapter.ProductViewHolder>  {

    private Context mCtx;
    private ArrayList<Cheque> productList;


    public Chequeadapter(Context mCtx, ArrayList<Cheque> pproductList) {
        this.mCtx = mCtx;
        productList=pproductList;

    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.asd, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
      final   Cheque cheque;
      cheque = productList.get(position);

        //loading the image

        holder.text.setText(cheque.getPrize());
       // Toast.makeText(mCtx,cheque.getStatus(),Toast.LENGTH_LONG).show();
if(cheque.getStatus().equals("credit"))
{
    holder.txtt.setTextColor(Color.GREEN);
    holder.txtt.setText("\u20B9"+cheque.getImage().toUpperCase());
}
else
{
    holder.txtt.setTextColor(Color.RED);
    holder.txtt.setText("\u20B9"+cheque.getImage().toUpperCase());
}
holder.ts.setText(cheque.getStatus());
holder.tt.setText(cheque
.getUser());

      //  SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Creating editor to store values to shared preferences


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


//    public void filterList(ArrayList<Cheque> filteredList) {
//        productList = filteredList;
//        notifyDataSetChanged();
//    }



    class ProductViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView text,txtt,ts,tt;
Button cart;
        public ProductViewHolder(View itemView) {
            super(itemView);
           // imageView = itemView.findViewById(R.id.imageView211);
            text=itemView.findViewById(R.id.textView7);
            txtt=itemView.findViewById(R.id.textView6);
            ts=itemView.findViewById(R.id.textView8);
            tt=itemView.findViewById(R.id.textView9);
        }

    }
    public void filterList(ArrayList<Cheque> filteredList) {
        productList = filteredList;
        notifyDataSetChanged();
    }

    }
