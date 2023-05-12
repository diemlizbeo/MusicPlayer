package com.example.musicplayer.fragment;

import static com.example.musicplayer.MainActivity.musicFiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.MusicAdapter;
import com.example.musicplayer.model.MusicFile;

import java.util.ArrayList;

public class SongFragment extends Fragment {
    private RecyclerView recyclerView;
    public static MusicAdapter musicAdapter;
    private SearchView searchView;

    public SongFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        recyclerView.setHasFixedSize(true);
        if(!(musicFiles.size() < 1 )){
            musicAdapter = new MusicAdapter(getContext(), musicFiles);
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
                for(MusicFile i : musicFiles){
                    if((i.getTitle().toLowerCase().contains(newText.toLowerCase()))){
                        filterList.add(i);
                    }
                }
                if (filterList.size() > 0)
                    SongFragment.musicAdapter.updateList(filterList);
                return false;
            }

            private void filter(String s) {
                ArrayList<MusicFile> filterList = new ArrayList<>();
                for(MusicFile i : musicFiles){
                    if((i.getTitle().toLowerCase().contains(s.toLowerCase()))){
                        filterList.add(i);
                    }
                }
                musicAdapter = new MusicAdapter(getContext(), filterList);
                recyclerView.setAdapter(musicAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
            }
        });
        return view;
    }
}
