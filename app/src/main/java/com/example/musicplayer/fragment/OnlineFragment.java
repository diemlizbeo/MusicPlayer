package com.example.musicplayer.fragment;


import static com.example.musicplayer.HomeActivity.listMusicOnline;
import static com.example.musicplayer.HomeActivity.musicFiles;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.MusicAdapter;
import com.example.musicplayer.adapter.MusicOnlineAdapter;
import com.example.musicplayer.model.MusicFile;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class OnlineFragment extends Fragment {

    private RecyclerView recyclerView;
    public static MusicOnlineAdapter musicAdapter;
    private FirebaseUser firebaseUser;
    private SearchView searchView;

    public OnlineFragment() {
        // Required empty public constructor
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        recyclerView.setHasFixedSize(true);
        if(!(listMusicOnline.size() < 1 )){
            Toast.makeText(getContext(),String.valueOf(listMusicOnline.size()) , Toast.LENGTH_SHORT).show();
            musicAdapter = new MusicOnlineAdapter(getContext(), listMusicOnline);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));


        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<MusicFile> filterList = new ArrayList<>();
                for(MusicFile i : listMusicOnline){
                    if((i.getTitle().toLowerCase().contains(newText.toLowerCase()))){
                        filterList.add(i);
                    }
                }
                if (filterList.size() > 0)
                    OnlineFragment.musicAdapter.updateList(filterList);
                return false;
            }

        });
        return view;
    }
}