package com.alramlawi.schoolapp.admin.studentstat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StudentStatViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StudentStatViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is students states fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}