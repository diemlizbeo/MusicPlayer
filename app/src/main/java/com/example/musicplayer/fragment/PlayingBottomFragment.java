package com.example.musicplayer.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.musicplayer.MainActivity.ARTIST_TO_FRAG;
import static com.example.musicplayer.MainActivity.PATH_TO_FRAG;
import static com.example.musicplayer.MainActivity.SHOW_MINI_PLAYER;
import static com.example.musicplayer.MainActivity.SONG_NAME;
import static com.example.musicplayer.MainActivity.SONG_NAME_TO_FRAG;
import static com.example.musicplayer.MainActivity.artists;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicplayer.MusicService;
import com.example.musicplayer.R;

import java.io.IOException;


public class PlayingBottomFragment extends Fragment implements ServiceConnection {
    private ImageView btNext, btPrevious,btPause,img;
    TextView tvName, tvArtist;
    View view;
    MusicService musicService;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";


    public PlayingBottomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_playing_bottom, container, false);
        tvArtist = view.findViewById(R.id.tvArtist);
        tvName = view.findViewById(R.id.tvName);
        img = view.findViewById(R.id.img);
        btNext = view.findViewById(R.id.btNext);
        btPause = view.findViewById(R.id.btPause);
        btPrevious = view.findViewById(R.id.btPrevious);
        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService != null){
                    musicService.PauseClicked();
                    if(musicService.isPlaying()){
                        btPause.setBackgroundResource(R.drawable.ic_pause);

                    }else{
                        btPause.setBackgroundResource(R.drawable.ic_play);
                    }



                }
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.NextClicked();
                if(getActivity() != null){
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                    editor.putString(MUSIC_FILE, musicService.musicFiles.get(musicService.position).getPath());
                    editor.putString(SONG_NAME, musicService.musicFiles.get(musicService.position).getTitle());
                    editor.putString(ARTIST_NAME, musicService.musicFiles.get(musicService.position).getArtist());
                    editor.apply();
                    SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED,MODE_PRIVATE);
                    String path = preferences.getString(MUSIC_FILE, null);
                    String artist = preferences.getString(ARTIST_NAME,null);
                    String song_name = preferences.getString(SONG_NAME,null);
                    if(path != null){
                        SHOW_MINI_PLAYER = true;
                        PATH_TO_FRAG = path;
                        ARTIST_TO_FRAG = artist;
                        SONG_NAME_TO_FRAG = song_name;
                    }else{
                        SHOW_MINI_PLAYER = false;
                        PATH_TO_FRAG = null;
                        ARTIST_TO_FRAG = null;
                        SONG_NAME_TO_FRAG = null;
                    }
                    if(SHOW_MINI_PLAYER == true){
                        if(PATH_TO_FRAG != null)
                        {
                            byte[] art = getAlbumArt(PATH_TO_FRAG);
                            if(art != null)
                                Glide.with(getContext()).load(art).into(img);
                            else
                                Glide.with(getContext()).load(R.drawable.nana).into(img);

                            tvName.setText(SONG_NAME_TO_FRAG);
                            tvArtist.setText(ARTIST_TO_FRAG);
                        }
                    }
                }
            }
        });

        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.PreviousClicked();
                if(getActivity() != null){
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                    editor.putString(MUSIC_FILE, musicService.musicFiles.get(musicService.position).getPath());
                    editor.putString(SONG_NAME, musicService.musicFiles.get(musicService.position).getTitle());
                    editor.putString(ARTIST_NAME, musicService.musicFiles.get(musicService.position).getArtist());
                    editor.apply();
                    SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED,MODE_PRIVATE);
                    String path = preferences.getString(MUSIC_FILE, null);
                    String artist = preferences.getString(ARTIST_NAME,null);
                    String song_name = preferences.getString(SONG_NAME,null);
                    if(path != null){
                        SHOW_MINI_PLAYER = true;
                        PATH_TO_FRAG = path;
                        ARTIST_TO_FRAG = artist;
                        SONG_NAME_TO_FRAG = song_name;
                    }else{
                        SHOW_MINI_PLAYER = false;
                        PATH_TO_FRAG = null;
                        ARTIST_TO_FRAG = null;
                        SONG_NAME_TO_FRAG = null;
                    }
                    if(SHOW_MINI_PLAYER == true){
                        if(PATH_TO_FRAG != null)
                        {
                            byte[] art = getAlbumArt(PATH_TO_FRAG);
                            if(art != null)
                                Glide.with(getContext()).load(art).into(img);
                            else
                                Glide.with(getContext()).load(R.drawable.dianhac).into(img);

                            tvName.setText(SONG_NAME_TO_FRAG);
                            tvArtist.setText(ARTIST_TO_FRAG);
                        }
                    }
                }

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SHOW_MINI_PLAYER == true){
            if(PATH_TO_FRAG != null)
            {
                byte[] art = getAlbumArt(PATH_TO_FRAG);
                if(art != null)
                    Glide.with(getContext()).load(art).into(img);
                else
                    Glide.with(getContext()).load(R.drawable.dianhac).into(img);
                btPause.setBackgroundResource(R.drawable.ic_pause);
                tvName.setText(SONG_NAME_TO_FRAG);
                tvArtist.setText(ARTIST_TO_FRAG);
                Intent intent = new Intent(getContext(), MusicService.class);
                if(getContext() != null){
                    getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(getContext() != null){
            getContext().unbindService(this);
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder)service;
        musicService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;

    }
}