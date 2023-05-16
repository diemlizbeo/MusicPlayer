package com.example.musicplayer;

import static com.example.musicplayer.HomeActivity.listalbumOnline;
import static com.example.musicplayer.HomeActivity.listartistOnline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayer.adapter.AlbumOnlineAdapter;
import com.example.musicplayer.adapter.ArtistOnlineAdapter;

public class ArtistOnlineActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArtistOnlineAdapter artistOnlineAdapter;
    private ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_online);
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        recyclerView.setHasFixedSize(true);
        if(!(listartistOnline.size() < 1 )){
            Toast.makeText(ArtistOnlineActivity.this,String.valueOf(listartistOnline.size()) , Toast.LENGTH_SHORT).show();
            artistOnlineAdapter = new ArtistOnlineAdapter(ArtistOnlineActivity.this, listartistOnline);
            recyclerView.setAdapter(artistOnlineAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));


        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}