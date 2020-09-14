package com.alramlawi.schoolapp.parent.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ViewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is parent assignments viewing fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}