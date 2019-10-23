package com.example.sensoresAlhambra.ui.navigation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NavigationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is navigation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}