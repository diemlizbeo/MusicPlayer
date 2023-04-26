package com.example.musicplayer.model;

import android.media.MediaPlayer;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

public class GalleryView extends ViewModel {
    private MutableLiveData<String> mText;

    public GalleryView() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");

    }

    public MutableLiveData<String> getText() {
        return mText;
    }

    public void setText(MutableLiveData<String> mText) {
        this.mText = mText;
    }

}
