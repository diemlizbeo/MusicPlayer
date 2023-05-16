package com.example.musicplayer;

import static com.example.musicplayer.HomeActivity.listMusicOnline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayer.adapter.MusicOnlineAdapter;
import com.google.firebase.auth.FirebaseUser;

public class AllMusicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MusicOnlineAdapter musicAdapter;
    private ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_music);
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        recyclerView.setHasFixedSize(true);
        if(!(listMusicOnline.size() < 1 )){
            Toast.makeText(AllMusicActivity.this,String.valueOf(listMusicOnline.size()) , Toast.LENGTH_SHORT).show();
            musicAdapter = new MusicOnlineAdapter(AllMusicActivity.this, listMusicOnline);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(AllMusicActivity.this,RecyclerView.VERTICAL,false));


        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}