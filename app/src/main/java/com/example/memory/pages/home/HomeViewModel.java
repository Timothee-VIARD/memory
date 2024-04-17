package com.example.memory.pages.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * Constructor for HomeViewModel
     */
    public HomeViewModel() {
        mText = new MutableLiveData<>();

    }

    /**
     * Get the text
     * @return the text
     */
    public LiveData<String> getText() {
        return mText;
    }
}