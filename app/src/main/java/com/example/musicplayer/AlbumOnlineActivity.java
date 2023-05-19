package com.example.musicplayer;

import static com.example.musicplayer.HomeActivity.listMusicTrend;
import static com.example.musicplayer.HomeActivity.listalbumOnline;

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
import com.example.musicplayer.adapter.MusicOnlineAdapter;

public class AlbumOnlineActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AlbumOnlineAdapter albumOnlineAdapter;
    private ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_online);
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        recyclerView.setHasFixedSize(true);
        if(!(listalbumOnline.size() < 1 )){
            albumOnlineAdapter = new AlbumOnlineAdapter(AlbumOnlineActivity.this, listalbumOnline);
            recyclerView.setAdapter(albumOnlineAdapter);
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