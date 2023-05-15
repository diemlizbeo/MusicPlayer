package com.example.musicplayer.fragment;


import static com.example.musicplayer.HomeActivity.listMusicOnline;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.MusicOnlineAdapter;
import com.google.firebase.auth.FirebaseUser;

public class OnlineFragment extends Fragment {

    private RecyclerView recyclerView;
    private MusicOnlineAdapter musicAdapter;
    private FirebaseUser firebaseUser;

    public OnlineFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        if(!(listMusicOnline.size() < 1 )){
            Toast.makeText(getContext(),String.valueOf(listMusicOnline.size()) , Toast.LENGTH_SHORT).show();
            musicAdapter = new MusicOnlineAdapter(getContext(), listMusicOnline);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));


        }
        return view;
    }
}