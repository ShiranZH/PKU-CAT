package com.example.pkucat.archive;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArchiveViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ArchiveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is archive fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
