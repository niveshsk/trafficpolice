package com.e.trafficpolice;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CustomLis extends ArrayAdapter {
    private ArrayList<Uri> arrayList;
    private Context context;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();

     CustomLis( Context context,ArrayList<Uri> arrayList) {
         super(context,R.layout.layout,arrayList);
         this.arrayList=arrayList;
         this.context=context;

     }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView=layoutInflater.inflate(R.layout.layout,null);
        final ImageView imageView=convertView.findViewById(R.id.display);
        TextView city_traf=convertView.findViewById(R.id.trf_sig);
        TextView cap_ti=convertView.findViewById(R.id.cap_ti);

        TextView imid=convertView.findViewById(R.id.imid);

        String[] urr=arrayList.get(position).toString().split("/",10);
        String[] hii=urr[7].split(".jpg",2);

        System.out.println(hii[0]);
        String[] city=hii[0].split("%2F",2);
        String trafficsignal=city[0];
        System.out.println(city[0]);
        String ti_cap=city[1];
        imid.setText(city[1]);

        city_traf.setText(trafficsignal.toUpperCase());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MMMM,dd//hh-mm-ss a");
        Date date = null;
        Date datnow=new Date(System.currentTimeMillis());

        try {
            date = sdf.parse(ti_cap);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTime = sdf.format(date);



        String formt=sdf1.format(date);
        String cu_ti=sdf1.format(datnow);
        long diff=datnow.getTime()- date.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String difftime=diffDays+" days,\n "+diffHours+" hrs, "+diffMinutes+" min, "+diffSeconds+" sec";
        cap_ti.setText(difftime);
System.out.println(difftime);
        Picasso.get().load(arrayList.get(position)).into(imageView);
        return convertView;
    }
}

