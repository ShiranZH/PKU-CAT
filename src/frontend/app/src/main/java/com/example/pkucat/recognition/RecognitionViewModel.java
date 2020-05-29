package com.example.pkucat.recognition;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecognitionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecognitionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Function recognition is to be implemented!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}