package com.example.musicplayer;

import static com.example.musicplayer.MainActivity.musicFiles;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.musicplayer.model.MusicFile;

import java.util.ArrayList;

public class PlayerActivity2 extends AppCompatActivity {

    private Button btNext, btPrevious, btPause, btShuffle, btRepeat;
    private TextView songLabel, singer, duration_played, duration_total;
    private SeekBar seekBar;
    private ImageView image;
    private static MediaPlayer mediaPlayer;
    private int position = -1;
    private String sname;
    static Uri uri;
    private static ArrayList<MusicFile> listSongs = new ArrayList<>();
    private Thread updateSeekBar, playThread, prevThread,nextThread;

    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

//        getSupportActionBar().setTitle("Now Playing");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btNext = findViewById(R.id.btNext);
        image = findViewById(R.id.img);
        btPause = findViewById(R.id.btPause);
        btShuffle = findViewById(R.id.btShuffle);
        btRepeat = findViewById(R.id.btRepeat);
        btPrevious = findViewById(R.id.btPrevious);
        songLabel = findViewById(R.id.songLabel);
        singer = findViewById(R.id.tvSinger);
        duration_played = findViewById(R.id.tvDuration_played);
        duration_total = findViewById(R.id.tvDurationtotal);
        seekBar = findViewById(R.id.seekBar);

        getIntentMethod();
        songLabel.setText(listSongs.get(position).getTitle());
        singer.setText(listSongs.get(position).getArtist());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity2.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {

                    int cur = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(cur);
                    duration_played.setText(formattedTime(cur));
                }
                handler.postDelayed(this, 1000);
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                seekBar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying()){
                    btPause.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }else{
                    btPause.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    position = ((position+1)%listSongs.size());
                    uri = Uri.parse(listSongs.get(position).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    metaData(uri);
                    songLabel.setText(listSongs.get(position).getTitle());
                    singer.setText(listSongs.get(position).getArtist());
                    seekBar.setMax(mediaPlayer.getDuration()/1000);
                    PlayerActivity2.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayer != null){

                                int cur = mediaPlayer.getCurrentPosition()/1000;
                                seekBar.setProgress(cur);
                                duration_played.setText(formattedTime(cur));
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                    btPause.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }else{
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    position = ((position+1)%listSongs.size());
                    uri = Uri.parse(listSongs.get(position).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    metaData(uri);
                    songLabel.setText(listSongs.get(position).getTitle());
                    singer.setText(listSongs.get(position).getArtist());
                    seekBar.setMax(mediaPlayer.getDuration()/1000);
                    PlayerActivity2.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayer != null){
                                int cur = mediaPlayer.getCurrentPosition()/1000;
                                seekBar.setProgress(cur);
                                duration_played.setText(formattedTime(cur));
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                    btPause.setBackgroundResource(R.drawable.ic_play);
                }
            }
        });

//        updateSeekBar = new Thread(){
//            @Override
//            public void run() {
//
//                if(mediaPlayer != null){
//
//                    int cur = mediaPlayer.getCurrentPosition()/1000;
//                    seekBar.setProgress(cur);
//                    duration_played.setText(formattedTime(cur));
//                }
//                handler.postDelayed(this, 1000);
//
//
//            }
//        };


//        updateSeekBar.start();
        // change color of seek-bar
//        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.MULTIPLY);
//        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.pink), PorterDuff.Mode.SRC_IN);
    }



    private String formattedTime(int cur) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(cur%60);
        String min = String.valueOf(cur/60);
        totalOut = min + ":" + seconds;
        totalNew = min + ":" + "0" + seconds;
        if(seconds.length() == 1)
            return totalNew;
        else return totalOut;
    }


    @Override
    protected void onResume() {
        nextThreadrun();
        prevThreadrun();
//        playThreadrun();
        super.onResume();
    }

    private void playThreadrun() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        playThread.run();
    }
    private void prevThreadrun() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            position = ((position-1) < 0) ?  (listSongs.size()-1):position-1;
                            uri = Uri.parse(listSongs.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            songLabel.setText(listSongs.get(position).getTitle());
                            singer.setText(listSongs.get(position).getArtist());
                            seekBar.setMax(mediaPlayer.getDuration()/1000);
                            PlayerActivity2.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(mediaPlayer != null){

                                        int cur = mediaPlayer.getCurrentPosition()/1000;
                                        seekBar.setProgress(cur);
                                        duration_played.setText(formattedTime(cur));
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                            btPause.setBackgroundResource(R.drawable.ic_pause);
                            mediaPlayer.start();
                        }else {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            position = ((position-1) < 0) ?  (listSongs.size()-1):position-1;
                            uri = Uri.parse(listSongs.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            songLabel.setText(listSongs.get(position).getTitle());
                            singer.setText(listSongs.get(position).getArtist());
                            seekBar.setMax(mediaPlayer.getDuration() / 1000);
                            PlayerActivity2.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int cur = mediaPlayer.getCurrentPosition() / 1000;
                                        seekBar.setProgress(cur);
                                        duration_played.setText(formattedTime(cur));
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                            btPause.setBackgroundResource(R.drawable.ic_play);
                        }
                    }
                });
            }
        };
        prevThread.run();
    }

    private void nextThreadrun() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            position = ((position+1)%listSongs.size());
                            uri = Uri.parse(listSongs.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            songLabel.setText(listSongs.get(position).getTitle());
                            singer.setText(listSongs.get(position).getArtist());
                            seekBar.setMax(mediaPlayer.getDuration()/1000);
                            PlayerActivity2.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(mediaPlayer != null){

                                        int cur = mediaPlayer.getCurrentPosition()/1000;
                                        seekBar.setProgress(cur);
                                        duration_played.setText(formattedTime(cur));
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                            btPause.setBackgroundResource(R.drawable.ic_pause);
                            mediaPlayer.start();
                        }else{
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            position = ((position+1)%listSongs.size());
                            uri = Uri.parse(listSongs.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metaData(uri);
                            songLabel.setText(listSongs.get(position).getTitle());
                            singer.setText(listSongs.get(position).getArtist());
                            seekBar.setMax(mediaPlayer.getDuration()/1000);
                            PlayerActivity2.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(mediaPlayer != null){
                                        int cur = mediaPlayer.getCurrentPosition()/1000;
                                        seekBar.setProgress(cur);
                                        duration_played.setText(formattedTime(cur));
                                    }
                                    handler.postDelayed(this, 1000);
                                }
                            });
                            btPause.setBackgroundResource(R.drawable.ic_play);
                        }
                    }
                });
            }
        };
        nextThread.run();
    }



    private void getIntentMethod() {
        position = getIntent().getIntExtra("position",-1);
        listSongs = musicFiles;
        if(listSongs != null){
            btPause.setBackgroundResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());


        }

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }

        seekBar.setMax(mediaPlayer.getDuration()/1000);
        metaData(uri);

    }

    private void metaData(Uri uri){
        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.toString());
            int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
            duration_total.setText(formattedTime(durationTotal));
            byte[] img = retriever.getEmbeddedPicture();
            if(img != null){
                Glide.with(this).asBitmap().load(img).into(image);
            }else{
                Glide.with(this).asBitmap().load(R.drawable.music_player).into(image);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}