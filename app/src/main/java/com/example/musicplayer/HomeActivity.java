package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.adapter.FragmentAdapter;
import com.example.musicplayer.fragment.AlbumFragment;
import com.example.musicplayer.fragment.ArtistFragment;
import com.example.musicplayer.fragment.OnlineFragment;
import com.example.musicplayer.fragment.SongFragment;
import com.example.musicplayer.model.MusicFile;
import com.example.musicplayer.model.MusicTrend;
import com.example.musicplayer.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    //    private TabLayout tabLayout;
    private BottomNavigationView bottomNavigationView;
    private SearchView searchView;
    private StorageReference storageRef;

    private ImageView imgAvt;
    private TextView tvName,tvEmail;
    private FirebaseUser firebaseUser;
    static boolean shuffle = false, repeat = false;
    private NavigationView navigationView;
    public static final int REQUEST_CODE = 1;
    public  static ArrayList<MusicFile> albums = new ArrayList<>();
    public  static ArrayList<MusicFile> artists = new ArrayList<>();
    public static ArrayList<MusicFile> musicFiles = new ArrayList<>();
    public static ArrayList<MusicFile> listalbumOnline = new ArrayList<>();
    public static ArrayList<MusicFile> listartistOnline = new ArrayList<>();

    public static ArrayList<String> albumOnlinetmp = new ArrayList<>();
    public static ArrayList<String> artistOnlinetmp = new ArrayList<>();

    public static ArrayList<MusicFile> listMusicOnline = new ArrayList<>();

    public static ArrayList<MusicFile> listMusicTrend = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_actvity_main);
        navigationView = findViewById(R.id.nav_view);

        imgAvt = navigationView.getHeaderView(0).findViewById(R.id.imgAvt);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_home:{
                        Toast.makeText(HomeActivity.this, "HOME", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_account:{
                        Intent intent = new Intent(HomeActivity.this , MyProfileActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.nav_idol:{
                        Intent intent = new Intent(HomeActivity.this , IdolActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case  R.id.nav_logout:{
                        FirebaseAuth.getInstance().signOut();
                        Intent intent4 = new Intent(HomeActivity.this , LoginActivity.class);
                        startActivity(intent4);
                        break;
                    }
                }
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        showInforUser();
        ActivityCompat.requestPermissions(HomeActivity.this,
                permissions(),
                1);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Songs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMusicOnline.clear();
                albumOnlinetmp.clear();
                artistOnlinetmp.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MusicFile ms = snapshot.getValue(MusicFile.class);
                    listMusicOnline.add(ms);
                    if(!albumOnlinetmp.contains(ms.getAlbum())){
                        listalbumOnline.add(ms);
                        albumOnlinetmp.add(ms.getAlbum());
                    }
                    if(!artistOnlinetmp.contains(ms.getArtist())){
                        listartistOnline.add(ms);
                        artistOnlinetmp.add(ms.getArtist());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference referenceTrend = FirebaseDatabase.getInstance().getReference("Trend");
        referenceTrend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMusicTrend.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    MusicTrend song = snapshot1.getValue(MusicTrend.class);
                    DatabaseReference s = FirebaseDatabase.getInstance().getReference().child("Songs").child(String.valueOf(song.getIdSong()));
                    s.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            MusicFile music = snapshot.getValue(MusicFile.class);
                            if(music.getId() == song.getIdSong())
                                listMusicTrend.add(music);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        initView();

    }
    private void initView() {
        viewPager = findViewById(R.id.viewPager);
//        tabLayout = findViewById(R.id.tabLayout);
        searchView = findViewById(R.id.searchView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        FragmentManager manager = getSupportFragmentManager();
        FragmentAdapter adapter = new FragmentAdapter(manager,4);
        viewPager.setAdapter(adapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.mInternet:viewPager.setCurrentItem(0);
                        break;
                    case R.id.mPhone:viewPager.setCurrentItem(1);
                        break;
                    case R.id.mAccount:viewPager.setCurrentItem(2);
                        break;
                    case R.id.mIdol: viewPager.setCurrentItem(3);
                        break;


                }
                return true;
            }
        });
//        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:bottomNavigationView.getMenu().findItem(R.id.mInternet).setChecked(true);

                        break;

                    case 1:bottomNavigationView.getMenu().findItem(R.id.mPhone).setChecked(true);

                        break;

                    case 2:bottomNavigationView.getMenu().findItem(R.id.mAccount).setChecked(true);

                        break;
                    case 3:bottomNavigationView.getMenu().findItem(R.id.mIdol).setChecked(true);

                        break;

                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                musicFiles = getAllAudio1(this); // display song list, when permission granted.
                initView();


            }else{
                ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

            }
        }
    }

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }
    @SuppressLint("ResourceType")
    private void showInforUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvName.setText(user.getFullname());
                tvEmail.setText(firebaseUser.getEmail());
                Picasso.get().load(user.getImageurl()).placeholder(R.drawable.avt).into(imgAvt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public  static  ArrayList<MusicFile> getAllAudio1 (Context context){
        ArrayList<MusicFile> tempAudioList = new ArrayList<>();
        ArrayList<String> duplicate = new ArrayList<>();
        ArrayList<String> arti = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };
        Cursor cursor = context.getContentResolver().query(uri, projection,null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                long id = Long.parseLong(cursor.getString(5));

                MusicFile musicFile = new MusicFile(path,title,artist,album,duration,id);
                tempAudioList.add(musicFile);

                if(!duplicate.contains(album)){
                    albums.add(musicFile);
                    duplicate.add(album);
                }
                if(!arti.contains(artist)){
                    artists.add(musicFile);
                    arti.add(artist);
                }


            }
            cursor.close();
        }
        return tempAudioList;
    }

}