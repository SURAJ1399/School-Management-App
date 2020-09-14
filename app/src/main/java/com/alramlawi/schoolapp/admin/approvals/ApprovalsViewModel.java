package com.alramlawi.schoolapp.admin.approvals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ApprovalsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ApprovalsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is approvals fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}