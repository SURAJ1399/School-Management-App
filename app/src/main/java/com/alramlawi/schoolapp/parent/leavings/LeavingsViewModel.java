package com.alramlawi.schoolapp.parent.leavings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeavingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LeavingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is parent leaving requests fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}