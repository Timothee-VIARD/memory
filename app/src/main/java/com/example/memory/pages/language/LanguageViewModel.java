package com.example.memory.pages.language;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LanguageViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * Constructor
     * Set the text
     */
    public LanguageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    /**
     * Get the text
     * @return the text
     */
    public LiveData<String> getText() {
        return mText;
    }
}