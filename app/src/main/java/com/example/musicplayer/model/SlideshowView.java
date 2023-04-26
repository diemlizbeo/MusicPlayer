package com.example.musicplayer.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowView extends ViewModel {
    private MutableLiveData<String> mText;

    public SlideshowView() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");

    }

    public MutableLiveData<String> getText() {
        return mText;
    }

    public void setText(MutableLiveData<String> mText) {
        this.mText = mText;
    }
}
