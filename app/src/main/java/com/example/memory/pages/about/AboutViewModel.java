package com.example.memory.pages.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * Constructor for the AboutViewModel class.
     */
    public AboutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    /**
     * Getter for the text.
     * @return the text.
     */
    public LiveData<String> getText() {
        return mText;
    }
}