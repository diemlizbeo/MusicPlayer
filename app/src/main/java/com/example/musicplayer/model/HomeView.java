package com.example.musicplayer.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeView extends ViewModel {
    private MutableLiveData<String> mText;

    public HomeView() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

    }

    public MutableLiveData<String> getText() {
        return mText;
    }

    public void setText(MutableLiveData<String> mText) {
        this.mText = mText;
    }
}
