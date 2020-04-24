<<<<<<< HEAD
package com.example.pkucat.ui.recognition;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecognitionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecognitionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is recogntion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
=======
package com.example.pkucat.ui.recognition;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecognitionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecognitionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is recogntion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
>>>>>>> e2d708ed7687c459bea18b7d72b70579e7b4d609
