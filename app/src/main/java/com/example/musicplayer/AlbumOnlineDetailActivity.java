package com.example.musicplayer;


import static com.example.musicplayer.HomeActivity.listMusicOnline;

import android.annotation.SuppressLint;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.adapter.AlbumDetailAdapter;
import com.example.musicplayer.adapter.AlbumOnlineAdapter;
import com.example.musicplayer.adapter.AlbumOnlineDetailAdapter;
import com.example.musicplayer.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumOnlineDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView imgAlbum;
    private String albumName;
    private ImageView back;
    private TextView tvnameAlbum;
    ArrayList<MusicFile> albumSongs = new ArrayList<>();
    AlbumOnlineDetailAdapter albumDetailAdapter;
    private TextView slSong;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        recyclerView = findViewById(R.id.recyclerView);
        imgAlbum = findViewById(R.id.imgAlbum);
        back = findViewById(R.id.back);
        slSong = findViewById(R.id.slSong);
        tvnameAlbum = findViewById(R.id.nameAlbum);
        albumName = getIntent().getStringExtra("albumonlinename");
        tvnameAlbum.setText(albumName);
        int j=0;
        for(int i=0;i<listMusicOnline.size();i++){
            if(albumName.equals(listMusicOnline.get(i).getAlbum())){
                albumSongs.add(j, listMusicOnline.get(i));
                j++;
            }
        }
        slSong.setText(albumSongs.size() + " bài hát");
        byte[] img = getAlbumArt(albumSongs.get(0).getPath());
        if(img != null){
            Glide.with(this).load(img).into(imgAlbum);
        }
        else{
            Glide.with(this).load(R.drawable.album).into(imgAlbum);

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(albumSongs.size() < 1)){
            albumDetailAdapter = new AlbumOnlineDetailAdapter(this, albumSongs);
            recyclerView.setAdapter(albumDetailAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        }
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return art;
    }
}