<<<<<<< HEAD
package com.example.pkucat.ui.archive;

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
=======
package com.example.pkucat.ui.archive;

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
>>>>>>> e2d708ed7687c459bea18b7d72b70579e7b4d609
