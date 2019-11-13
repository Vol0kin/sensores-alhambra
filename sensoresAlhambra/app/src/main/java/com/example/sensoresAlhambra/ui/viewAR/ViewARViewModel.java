package com.example.sensoresAlhambra.ui.viewAR;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewARViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ViewARViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is maps fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}