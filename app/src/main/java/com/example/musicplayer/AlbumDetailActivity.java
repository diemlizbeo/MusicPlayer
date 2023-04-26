package com.example.musicplayer;

import static com.example.musicplayer.MainActivity.musicFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.adapter.AlbumDetailAdapter;
import com.example.musicplayer.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView imgAlbum;
    private String albumName;
    ArrayList<MusicFile> albumSongs = new ArrayList<>();
    AlbumDetailAdapter albumDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        recyclerView = findViewById(R.id.recyclerView);
        imgAlbum = findViewById(R.id.imgAlbum);
        albumName = getIntent().getStringExtra("albumname");
        int j=0;
        for(int i=0;i<musicFiles.size();i++){
            if(albumName.equals(musicFiles.get(i).getAlbum())){
                albumSongs.add(j, musicFiles.get(i));
                j++;
            }
        }
        byte[] img = getAlbumArt(albumSongs.get(0).getPath());
        if(img != null){
            Glide.with(this).load(img).into(imgAlbum);
        }
        else{
            Glide.with(this).load(R.drawable.logo).into(imgAlbum);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(albumSongs.size() < 1)){
            albumDetailAdapter = new AlbumDetailAdapter(this, albumSongs);
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