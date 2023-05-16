package com.example.musicplayer;





import static com.example.musicplayer.HomeActivity.listMusicOnline;
import static com.example.musicplayer.HomeActivity.musicFiles;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.adapter.ArtistDetailAdapter;
import com.example.musicplayer.adapter.ArtistOnlineDetailAdapter;
import com.example.musicplayer.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;

public class ArtistOnlineDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView imgAlbum;
    private String artistName;
    private TextView tvnameArtist;
    private ImageView back;
    ArrayList<MusicFile> artistSongs = new ArrayList<>();
    ArtistOnlineDetailAdapter artistDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        recyclerView = findViewById(R.id.recyclerView);
        imgAlbum = findViewById(R.id.imgAlbum);
        tvnameArtist = findViewById(R.id.nameArtist);
        back = findViewById(R.id.back);
        artistName = getIntent().getStringExtra("artistonlinename");
        tvnameArtist.setText(artistName);
        int j=0;
        for(int i=0;i<listMusicOnline.size();i++){
            if(artistName.equals(listMusicOnline.get(i).getArtist())){
                artistSongs.add(j, listMusicOnline.get(i));
                j++;
            }
        }
        byte[] img = getAlbumArt(artistSongs.get(0).getPath());
        if(img != null){
            Glide.with(this).load(img).into(imgAlbum);
        }
        else{
            Glide.with(this).load(R.drawable.logo).into(imgAlbum);

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
        if(!(artistSongs.size() < 1)){
            artistDetailAdapter = new ArtistOnlineDetailAdapter(this, artistSongs);
            recyclerView.setAdapter(artistDetailAdapter);
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