package com.example.musicplayer;

import static com.example.musicplayer.ApplicationClass.ACTION_NEXT;
import static com.example.musicplayer.ApplicationClass.ACTION_PLAY;
import static com.example.musicplayer.ApplicationClass.ACTION_PREVIOUS;
import static com.example.musicplayer.ApplicationClass.CHANNEL_ID_2;
import static com.example.musicplayer.MainActivity.repeat;
import static com.example.musicplayer.MainActivity.shuffle;
import static com.example.musicplayer.adapter.AlbumDetailAdapter.listAlbum;
import static com.example.musicplayer.adapter.ArtistDetailAdapter.listArtist;
import static com.example.musicplayer.adapter.MusicAdapter.listFile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicplayer.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity implements  ActionPlayingInterface, ServiceConnection {

    private Button btNext, btPrevious, btPause, btShuffle, btRepeat;
    private TextView songLabel, singer, duration_played, duration_total;
    private SeekBar seekBar;
    private ImageView image;
//    public static MediaPlayer mediaPlayer;
    private int position = -1;

    public static Uri uri;
    public static ArrayList<MusicFile> listSongs = new ArrayList<>();
    private Thread updateSeekBar, playThread, prevThread,nextThread;
    MusicService musicService;

    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setFulScreen();
        setContentView(R.layout.activity_player);
//        getSupportActionBar().hide();

//        getSupportActionBar().setTitle("Now Playing");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);




        initView();
        getIntentMethod();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {

                    int cur = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(cur);
                    duration_played.setText(formattedTime(cur));
                }
                handler.postDelayed(this, 1000);
            }
        });

        btShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffle){
                    shuffle = false;
                    btShuffle.setBackgroundResource(R.drawable.ic_shuffle_off);
                }else{
                    shuffle = true;
                    btShuffle.setBackgroundResource(R.drawable.ic_shuffle_on);
                }
            }
        });
        btRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeat){
                    repeat = false;
                    btRepeat.setBackgroundResource(R.drawable.ic_repeat_off);

                }else{
                    repeat = true;
                    btRepeat.setBackgroundResource(R.drawable.ic_repeat_on);
                }
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                seekBar.setMax(mediaPlayer.getDuration());
                btPauseClicked();
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btNextClicked();
            }
        });
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPreviousClicked();
            }
        });

//        updateSeekBar.start();
        // change color of seek-bar
//        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.MULTIPLY);
//        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.pink), PorterDuff.Mode.SRC_IN);
    }

    private void setFulScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initView() {

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

    }

    public void btPauseClicked() {
        if(musicService.isPlaying()){
            btPause.setBackgroundResource(R.drawable.ic_play);
            musicService.showNotification("Play");
            musicService.pause();
            seekBar.setMax(musicService.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {

                        int cur = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(cur);
                        duration_played.setText(formattedTime(cur));
                    }
                    handler.postDelayed(this, 1000);
                }
            });


        }else{
            musicService.showNotification("Pause");
            btPause.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
            seekBar.setMax(musicService.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {

                        int cur = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(cur);
                        duration_played.setText(formattedTime(cur));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    public void btPreviousClicked() {
        if(musicService.isPlaying()){
            musicService.stop();
            musicService.release();
            if(shuffle && !repeat){
                position = getRandom(listSongs.size()-1);
            }else if(!shuffle && !repeat){
                position = ((position-1) < 0) ?  (listSongs.size()-1):position-1;
            }

            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songLabel.setText(listSongs.get(position).getTitle());
            singer.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService != null){

                        int cur = musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(cur);
                        duration_played.setText(formattedTime(cur));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            musicService.showNotification("Pause");
            btPause.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
        }else {
            musicService.stop();
            musicService.release();
            if(shuffle && !repeat){
                position = getRandom(listSongs.size()-1);
            }else if(!shuffle && !repeat){
                position = ((position-1) < 0) ?  (listSongs.size()-1):position-1;
            }

            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songLabel.setText(listSongs.get(position).getTitle());
            singer.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int cur = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(cur);
                        duration_played.setText(formattedTime(cur));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            musicService.showNotification("Play");
            btPause.setBackgroundResource(R.drawable.ic_play);
        }
    }

    public void btNextClicked() {
        if(musicService.isPlaying()){
            musicService.stop();
            musicService.release();
            if(shuffle && !repeat){
                position = getRandom(listSongs.size()-1);
            }else if(!shuffle && !repeat){
                position = ((position+1)%listSongs.size());
            }

            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songLabel.setText(listSongs.get(position).getTitle());
            singer.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService != null){

                        int cur = musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(cur);
                        duration_played.setText(formattedTime(cur));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            musicService.showNotification("Pause");
            btPause.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
        }else{
            musicService.stop();
            musicService.release();

            if(shuffle && !repeat){
                position = getRandom(listSongs.size()-1);
            }else if(!shuffle && !repeat){
                position = ((position+1)%listSongs.size());
            }


            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songLabel.setText(listSongs.get(position).getTitle());
            singer.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService != null){
                        int cur = musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(cur);
                        duration_played.setText(formattedTime(cur));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            musicService.showNotification("Play");
            btPause.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i+1);
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

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position",-1);
        String sender = getIntent().getStringExtra("sender");
        if(sender != null && sender.equals("albumDetails")){
            listSongs = listAlbum;
        } else if (sender != null && sender.equals("artistDetails")){
            listSongs = listArtist;
        }else{
            listSongs = listFile;
        }
        if(listSongs != null){
            btPause.setBackgroundResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());


        }

//        if(musicService != null){
//            musicService.stop();
//            musicService.release();
////            musicService.createMediaPlayer(position);
////            musicService.start();
//        }
//        musicService.createMediaPlayer(position);
//        musicService.start();

        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("servicePosition", position);
        startService(intent);


    }

    private void metaData(Uri uri){
        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.toString());
            int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
            duration_total.setText(formattedTime(durationTotal));
            byte[] img = retriever.getEmbeddedPicture();
            Bitmap bitmap ;

            if(img != null){
                Glide.with(this).asBitmap().load(img).into(image);

                bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
                ImgAnimation(this, image, bitmap);
//                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(@Nullable Palette palette) {
//                        Palette.Swatch swatch = palette.getDominantSwatch();
//                        if(swatch != null){
////                            ImageView gradient = findViewById(R.id.img);
////                            LinearLayout container = findViewById(R.id.linearLayout);
////                            gradient.setBackgroundResource(R.drawable.gradient_bg);
////                            container.setBackgroundResource(R.drawable.player_bg);
////                            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
////                                    new int[]{0xff000000, 0x00000000});
////                            gradient.setBackground(drawable);
////                            GradientDrawable drawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
////                                    new int[]{0xffffffff, 0xffffffff});
////                            container.setBackground(drawableBg);
//                            singer.setTextColor(Color.GRAY);
//                        }
//                    }
//                });
            }else{
                Glide.with(this).asBitmap().load(R.drawable.music_player).into(image);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void ImgAnimation(final Context context, final ImageView imageView, final Bitmap bitmap){
        Animation aniOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        final Animation aniIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        aniOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                aniIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(aniIn);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(aniOut);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        btNextClicked();
//        if(musicService != null){
//            musicService.createMediaPlayer(position);
//
////            /////////////
//            btPause.setBackgroundResource(R.drawable.ic_pause);
//            musicService.start();
//            musicService.onCompleted();
//        }
//
//    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        MusicService.MyBinder myBinder =(MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallBack(this);
        Toast.makeText(this, "Connected" + musicService, Toast.LENGTH_SHORT).show();
        seekBar.setMax(musicService.getDuration()/1000);
        metaData(uri);
        songLabel.setText(listSongs.get(position).getTitle());
        singer.setText(listSongs.get(position).getArtist());
        musicService.onCompleted();
        musicService.showNotification("Pause");

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;

    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent,this, BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unbindService(this);
    }


}