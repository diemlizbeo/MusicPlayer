package com.example.musicplayer.fragment;

import static com.example.musicplayer.HomeActivity.albums;
import static com.example.musicplayer.HomeActivity.artists;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicplayer.HomeActivity;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.model.MusicFile;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PhoneFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FirebaseUser firebaseUser;

    public static final int REQUEST_CODE = 1;
//    public  static ArrayList<MusicFile> albums = new ArrayList<>();
//    public  static ArrayList<MusicFile> artists = new ArrayList<>();
//    public static ArrayList<MusicFile> musicFiles = new ArrayList<>();
//
//    public static ArrayList<MusicFile> listMusicOnline = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_phone, container, false);
        initView(view);



        return view;
    }


    private void initView(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
//        searchView = findViewById(R.id.searchView);
//        bottomNavigationView = findViewById(R.id.bottomNavigation);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragments(new SongFragment(), "Song");
        viewPagerAdapter.addFragments(new AlbumFragment(), "Album");
        viewPagerAdapter.addFragments(new ArtistFragment(), "Artist");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }



}