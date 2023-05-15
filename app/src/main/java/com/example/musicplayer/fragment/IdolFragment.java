package com.example.musicplayer.fragment;

import static com.example.musicplayer.HomeActivity.musicFiles;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musicplayer.AddIdolActivity;
import com.example.musicplayer.IdolActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.IdolAdapter;
import com.example.musicplayer.adapter.MusicAdapter;
import com.example.musicplayer.model.Idol;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class IdolFragment extends Fragment {
    private FirebaseUser firebaseUser;

    private ArrayList<Idol> idolList;
    RecyclerView recyclerView;
    private IdolAdapter idolAdapter;
    SearchView searchView;
    private TextView tvAdd;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_idol, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        tvAdd = view.findViewById(R.id.tvAdd);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext() , 2);
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

        idolAdapter = new IdolAdapter(getContext() , idolList);
        recyclerView.setAdapter(idolAdapter);


        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddIdolActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}