package com.example.foodcommentmp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> usernameLiveData;
    private MutableLiveData<String> passwordLiveData;

    public LoginViewModel(){
        usernameLiveData = new MutableLiveData<>();
        usernameLiveData.setValue("");
        passwordLiveData = new MutableLiveData<>();
        passwordLiveData.setValue("");
    }

    public MutableLiveData<String> getUsernameLiveData() {
        return usernameLiveData;
    }

    public MutableLiveData<String> getPasswordLiveData() {
        return passwordLiveData;
    }

    public void recodeUserName(String username){
        usernameLiveData.setValue(username);
    }

    public void recodePassWord(String password){
        passwordLiveData.setValue(password);
    }

    public void record(String username, String password){
        usernameLiveData.setValue(username);
        passwordLiveData.setValue(password);

        System.out.println(usernameLiveData.getValue());
        System.out.println(passwordLiveData.getValue());
    }

    public void reset(){
        usernameLiveData.setValue("");
        passwordLiveData.setValue("");
    }
}
