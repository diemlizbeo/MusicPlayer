package com.example.musicplayer;

import static com.example.musicplayer.HomeActivity.listMusicOnline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayer.adapter.MusicOnlineAdapter;
import com.example.musicplayer.fragment.OnlineFragment;
import com.example.musicplayer.model.MusicFile;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AllMusicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static MusicOnlineAdapter musicAdapter;
    private ImageView back;
    private SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_music);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        back = findViewById(R.id.back);
        recyclerView.setHasFixedSize(true);
        if(!(listMusicOnline.size() < 1 )){
            Toast.makeText(AllMusicActivity.this,String.valueOf(listMusicOnline.size()) , Toast.LENGTH_SHORT).show();
            musicAdapter = new MusicOnlineAdapter(AllMusicActivity.this, listMusicOnline);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(AllMusicActivity.this,RecyclerView.VERTICAL,false));


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
//                if (filterList.size() > 0)
                AllMusicActivity.musicAdapter.updateList(filterList);
                return false;
            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}