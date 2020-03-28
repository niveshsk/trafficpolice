package com.e.trafficpolice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ref=findViewById(R.id.ref);
Button edge=findViewById(R.id.edge);
edge.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,Edge.class);
        startActivity(intent);
    }
});
        final DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("traffic").child("nagercoil").child("status");
firebaseDatabase.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue(String.class).toString().equals("update")){
            firebaseDatabase.setValue("displayed");
            int notificationId = 0;
            Intent intent = new Intent(MainActivity.this,Noti.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.activity_noti);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())

                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.common_full_open_on_phone))
                    .setContentTitle("traffic is violated")
                    .setContentText("In nagercoil click to more details")
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                            + "://" + getPackageName() + "/raw/dd"))
                    .setContentIntent(pendingIntent)
                    .setDefaults(NotificationCompat.BADGE_ICON_LARGE)
                    .setCustomBigContentView(notificationLayout);

            //set a tone when notification appears
            try {
                AssetFileDescriptor assetFileDescriptor=getAssets().openFd("raw/");

            } catch (IOException e) {
                e.printStackTrace();
            }


            //call notification manager so it can build and deliver the notification to the OS
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //Android 8 introduced a new requirement of setting the channelId property by using a NotificationChannel.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "10";
                NotificationChannel channel = new NotificationChannel(channelId,
                        "traffic",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }

            notificationManager.notify(notificationId,builder.build());
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("nagercoil");
            Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
            final ListView listView=findViewById(R.id.list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MainActivity.this, "fgdfgdf", Toast.LENGTH_SHORT).show();
                }
            });
            mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(final ListResult listResult) {
                    final int tot=listResult.getItems().toArray().length;
                    final ArrayList<Uri> arrayList = new ArrayList<Uri>(tot);

                    arrayList.clear();
                    int inu=tot-1;

                    while(inu > -1) {
                        final int dd=inu;
                        System.out.println(dd);
                        listResult.getItems().get(inu).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                arrayList.add(uri);
                                Collections.sort(arrayList);
                                if (arrayList.size() == tot ){
                                    Log.e("ghjj",arrayList.toString());
                                    CustomLis customLis=new CustomLis(MainActivity.this,arrayList);
                                    listView.setAdapter(customLis);
                                }

                            }
                        });
                        inu--;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "connection failed", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(MainActivity.this, "displayed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("nagercoil");

        final ListView listView=findViewById(R.id.list);
        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(final ListResult listResult) {
                final int tot=listResult.getItems().toArray().length;
                final ArrayList<Uri> arrayList = new ArrayList<Uri>(tot);
                arrayList.clear();
                int inu=tot-1;

                while(inu > -1) {
                    final int dd=inu;
                    System.out.println(dd);

                    listResult.getItems().get(inu).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            arrayList.add(uri);
                            if (arrayList.size() == tot ){
                                  Log.e("ghjj",arrayList.toString());
                                    CustomLis customLis=new CustomLis(MainActivity.this,arrayList);
                                listView.setAdapter(customLis);
                            }

                        }
                    });
inu--;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "connection failed", Toast.LENGTH_SHORT).show();
            }
        });


    }



}
