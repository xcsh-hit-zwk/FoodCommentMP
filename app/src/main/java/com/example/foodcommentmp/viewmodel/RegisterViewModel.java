package com.example.foodcommentmp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> usernameLiveData;
    private MutableLiveData<String> passwordLiveData;
    private MutableLiveData<String> nicknameLiveData;

    public RegisterViewModel(){
        usernameLiveData = new MutableLiveData<>();
        usernameLiveData.setValue("");
        passwordLiveData = new MutableLiveData<>();
        passwordLiveData.setValue("");
        nicknameLiveData = new MutableLiveData<>();
        nicknameLiveData.setValue("");
    }

    public MutableLiveData<String> getUsernameLiveData() {
        return usernameLiveData;
    }

    public MutableLiveData<String> getPasswordLiveData() {
        return passwordLiveData;
    }

    public MutableLiveData<String> getNicknameLiveData() {
        return nicknameLiveData;
    }

    public void recodeUserName(String username){
        usernameLiveData.setValue(username);
    }

    public void recodePassWord(String password){
        passwordLiveData.setValue(password);
    }

    public void recodeNickName(String nickname){
        nicknameLiveData.setValue(nickname);
    }

    public void record(String username, String password, String nickname){
        usernameLiveData.setValue(username);
        passwordLiveData.setValue(password);
        if(nickname == ""){
            nickname = username;
        }
        nicknameLiveData.setValue(nickname);

        System.out.println(usernameLiveData.getValue());
        System.out.println(passwordLiveData.getValue());
        System.out.println(nicknameLiveData.getValue());
    }

    public void reset(){
        usernameLiveData.setValue("");
        passwordLiveData.setValue("");
        nicknameLiveData.setValue("");
    }
}
