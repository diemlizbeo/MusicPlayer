package com.example.musicplayer.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicplayer.R;
import com.example.musicplayer.model.GalleryView;
import com.example.musicplayer.model.HomeView;

import java.io.IOException;

public class GalleryFragment extends Fragment {
    private GalleryView galleryView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        galleryView = new ViewModelProvider(this).get(GalleryView.class);
        View view=inflater.inflate(R.layout.fragment_gallery, container,false);
        final TextView tv = view.findViewById(R.id.txt);
        galleryView.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv.setText(s);
            }
        });
        return view;
    }
    public void play_Song(View v){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource("");
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
