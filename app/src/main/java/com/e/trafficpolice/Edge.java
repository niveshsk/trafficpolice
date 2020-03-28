package com.e.trafficpolice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class Edge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edge);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("nagercoil").child("edge");
        Toast.makeText(Edge.this, "updated", Toast.LENGTH_SHORT).show();
        final ListView listView = findViewById(R.id.edge_lis);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(final ListResult listResult) {
                final int tot = listResult.getItems().toArray().length;
                final ArrayList<Uri> arrayList = new ArrayList<Uri>(tot);

                arrayList.clear();
                int inu = tot - 1;

                while (inu > -1) {
                    final int dd = inu;
                    System.out.println(dd);
                    listResult.getItems().get(inu).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            arrayList.add(uri);
                            Collections.sort(arrayList);
                            if (arrayList.size() == tot) {
                                Log.e("ghjj", arrayList.toString());
                                CustEdge customLis = new CustEdge(Edge.this, arrayList);
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
                Toast.makeText(Edge.this, "connection failed", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
