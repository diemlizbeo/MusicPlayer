package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.fragment.AlbumFragment;
import com.example.musicplayer.fragment.ArtistFragment;
import com.example.musicplayer.fragment.OnlineFragment;
import com.example.musicplayer.fragment.SongFragment;
import com.example.musicplayer.model.MusicFile;
import com.example.musicplayer.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static boolean SHOW_MINI_PLAYER = false;
    private ViewPager viewPager;
//    private TabLayout tabLayout;
    private BottomNavigationView bottomNavigationView;
    private SearchView searchView;
    private ImageView imgAvt;
    private TextView tvName,tvEmail;
    private FirebaseUser firebaseUser;
    private StorageReference storageRef;
    public static final int REQUEST_CODE = 1;
//    public  static ArrayList<MusicFile> albums = new ArrayList<>();
//    public  static ArrayList<MusicFile> artists = new ArrayList<>();
//    public static ArrayList<MusicFile> musicFiles = new ArrayList<>();

    FrameLayout frame_player;

    private NavigationView navigationView;

    static boolean shuffle = false, repeat = false;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";

    public static  String PATH_TO_FRAG = null;
    public static  String ARTIST_TO_FRAG = null;
    public static  String SONG_NAME_TO_FRAG = null;

//    public static ArrayList<MusicFile> listMusicOnline = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_actvity_main);
//        navigationView = findViewById(R.id.nav_view);
//
//        initView();
//        imgAvt = navigationView.getHeaderView(0).findViewById(R.id.imgAvt);
//        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
//        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.nav_home:{
//                        Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    case R.id.nav_account:{
//                        Intent intent = new Intent(MainActivity.this , MyProfileActivity.class);
//                        startActivity(intent);
//                        break;
//                    }
//                    case R.id.nav_idol:{
//                        Intent intent = new Intent(MainActivity.this , IdolActivity.class);
//                        startActivity(intent);
//                        break;
//                    }
//                    case  R.id.nav_logout:{
//                        FirebaseAuth.getInstance().signOut();
//                        Intent intent4 = new Intent(MainActivity.this , LoginActivity.class);
//                        startActivity(intent4);
//                        break;
//                    }
//                }
//                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            }
//        });
//
//        showInforUser();
//        ActivityCompat.requestPermissions(MainActivity.this,
//                permissions(),
//                1);
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Songs");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                listMusicOnline.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    MusicFile idol = snapshot.getValue(MusicFile.class);
//                    listMusicOnline.add(idol);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void initView() {
//        viewPager = findViewById(R.id.viewPager);
////        tabLayout = findViewById(R.id.tabLayout);
//        searchView = findViewById(R.id.searchView);
//        bottomNavigationView = findViewById(R.id.bottomNavigation);
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.addFragments(new SongFragment(), "Song");
//        viewPagerAdapter.addFragments(new AlbumFragment(), "Album");
//        viewPagerAdapter.addFragments(new ArtistFragment(), "Artist");
//        viewPagerAdapter.addFragments(new OnlineFragment(), "Online");
//        viewPager.setAdapter(viewPagerAdapter);
//
////        tabLayout.setupWithViewPager(viewPager);
//
//
//    }
//
//    private void runPermission(){
//        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
//        }else{
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//            musicFiles = getAllAudio1(this); // display song list, when permission granted.
//            initView();
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == REQUEST_CODE){
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                musicFiles = getAllAudio1(this); // display song list, when permission granted.
//                initView();
//
//
//            }else{
//                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
//
//            }
//        }
//    }
//    public static String[] storge_permissions = {
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//    };
//
//    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
//    public static String[] storge_permissions_33 = {
//            Manifest.permission.READ_MEDIA_IMAGES,
//            Manifest.permission.READ_MEDIA_AUDIO,
//            Manifest.permission.READ_MEDIA_VIDEO
//    };
//
//    public static String[] permissions() {
//        String[] p;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            p = storge_permissions_33;
//        } else {
//            p = storge_permissions;
//        }
//        return p;
//    }
//
//    public static  class ViewPagerAdapter extends FragmentPagerAdapter{
//        private ArrayList<Fragment> fragments;
//        private ArrayList<String> titles;
//
//
//        public ViewPagerAdapter(@NonNull FragmentManager fm) {
//            super(fm);
//            this.fragments = new ArrayList<>();
//            this.titles = new ArrayList<>();
//        }
//
//        void addFragments(Fragment fragment, String title){
//            fragments.add(fragment);
//            titles.add(title);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return fragments.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return fragments.size();
//        }
//
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles.get(position);
//        }
//    }
//
//    public  static  ArrayList<MusicFile> getAllAudio1 (Context context){
//        ArrayList<MusicFile> tempAudioList = new ArrayList<>();
//        ArrayList<String> duplicate = new ArrayList<>();
//        ArrayList<String> arti = new ArrayList<>();
//
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {
//                MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.DURATION,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media._ID
//        };
//        Cursor cursor = context.getContentResolver().query(uri, projection,null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//        if(cursor != null){
//            while (cursor.moveToNext()){
//                String album = cursor.getString(0);
//                String title = cursor.getString(1);
//                String duration = cursor.getString(2);
//                String path = cursor.getString(3);
//                String artist = cursor.getString(4);
//                long id = Long.parseLong(cursor.getString(5));
//
//                MusicFile musicFile = new MusicFile(path,title,artist,album,duration,id);
//                tempAudioList.add(musicFile);
//
//                if(!duplicate.contains(album)){
//                    albums.add(musicFile);
//                    duplicate.add(album);
//                }
//                if(!arti.contains(artist)){
//                    artists.add(musicFile);
//                    arti.add(artist);
//                }
//
//
//            }
//            cursor.close();
//        }
//        return tempAudioList;
//    }
//
//    public static ArrayList<MusicFile> getAllAudio(Context context) {
//
//        ArrayList<MusicFile> musicInfos = new ArrayList<MusicFile>();
//
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//        if (cursor == null) {
//            return null;
//        }
//
//
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToNext();
//            ArrayList<String> duplicate = new ArrayList<>();
//
//            @SuppressLint("Range") int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
//
//            if (isMusic != 0) {
//                MusicFile music = new MusicFile();
//                music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
//                if (!new File(music.getPath()).exists()) {
//                    continue;
//                }
//                music.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
//                music.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
//                music.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
//                music.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
//                music.setId(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))));
//                musicInfos.add(music);
//
//                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
//
//                if(!duplicate.contains(album)){
//                    albums.add(music);
//                    duplicate.add(album);
//                }
//            }
//        }
//        return musicInfos;
//    }
//
//    @SuppressLint("ResourceType")
//    private void showInforUser(){
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        storageRef = FirebaseStorage.getInstance().getReference("uploads");
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                tvName.setText(user.getFullname());
//                tvEmail.setText(firebaseUser.getEmail());
//                Picasso.get().load(user.getImageurl()).placeholder(R.drawable.avt).into(imgAvt);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }


    }

}