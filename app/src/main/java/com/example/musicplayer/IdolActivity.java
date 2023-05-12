package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.musicplayer.adapter.IdolAdapter;
import com.example.musicplayer.model.Idol;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IdolActivity extends AppCompatActivity {

    Uri imageUri;
    String myUrl;
    StorageTask uploadTask;
    StorageReference storageReference;
    private FirebaseUser firebaseUser;

    private ArrayList<Idol> idolList;
    RecyclerView recyclerView;
    private IdolAdapter idolAdapter;
    SearchView searchView;
    private TextView tvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idol);
        recyclerView =findViewById(R.id.recycleView);
        tvAdd = findViewById(R.id.tvAdd);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext() , 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        idolList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Idols");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idolList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Idol idol = snapshot.getValue(Idol.class);
                    if (idol.getPublisher().equals(firebaseUser.getUid())){
                        idolList.add(idol);
                    }
                }

                Collections.reverse(idolList);
                idolAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        idolAdapter = new IdolAdapter(getApplicationContext() , idolList);
        recyclerView.setAdapter(idolAdapter);


        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdolActivity.this, AddIdolActivity.class);
                startActivity(intent);
            }
        });
    }
}