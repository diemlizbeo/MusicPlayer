package com.example.musicplayer.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.musicplayer.fragment.AccountFragment;
import com.example.musicplayer.fragment.IdolFragment;
import com.example.musicplayer.fragment.InternetFragment;
import com.example.musicplayer.fragment.PhoneFragment;


public class FragmentAdapter extends FragmentPagerAdapter {

    private int pageNum;

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.pageNum = behavior;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                InternetFragment fm1 = new InternetFragment();
                return fm1;
            case 1:
                PhoneFragment fm2 = new PhoneFragment();
                return fm2;
            case 2:
                AccountFragment fm3 = new AccountFragment();
                return fm3;
            case 3:
                IdolFragment fm4 = new IdolFragment();
                return fm4;
        }

        return null;
    }

    @Override
    public int getCount() {
        return pageNum;
    }


}
