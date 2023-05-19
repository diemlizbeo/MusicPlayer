package com.example.musicplayer;

import static com.example.musicplayer.HomeActivity.listMusicOnline;
import static com.example.musicplayer.HomeActivity.listMusicTrend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayer.adapter.MusicOnlineAdapter;
import com.google.firebase.auth.FirebaseUser;

public class TrendMusicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MusicOnlineAdapter musicAdapter;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_music);
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        recyclerView.setHasFixedSize(true);
        if(!(listMusicTrend.size() < 1 )){
            musicAdapter = new MusicOnlineAdapter(TrendMusicActivity.this, listMusicTrend);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(TrendMusicActivity.this,RecyclerView.VERTICAL,false));


        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}