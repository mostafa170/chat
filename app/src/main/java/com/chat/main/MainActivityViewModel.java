package com.chat.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chat.main.model.searchPeople.SearchPeapleResponse;
import com.chat.main.model.user.UserResponse;
import com.chat.main.model.userID.UserIDResponse;
import com.chat.network.DevartlinkAPI;
import com.chat.utils.UserPreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {

    protected MutableLiveData<Integer> errorMessage;
    protected MutableLiveData<SearchPeapleResponse> peapleResponseMutableLiveData;
    protected MutableLiveData<UserResponse> userResponseMutableLiveData;
    protected MutableLiveData<UserIDResponse> userIDResponseMutableLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        peapleResponseMutableLiveData = new MutableLiveData<>();
        userResponseMutableLiveData = new MutableLiveData<>();
        userIDResponseMutableLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<UserResponse> getUserResponseMutableLiveData() {
        return userResponseMutableLiveData;
    }

    public MutableLiveData<Integer> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<UserIDResponse> getUserIDResponseMutableLiveData() {
        return userIDResponseMutableLiveData;
    }

    public MutableLiveData<SearchPeapleResponse> getPeapleResponseMutableLiveData() {
        return peapleResponseMutableLiveData;
    }
    public void getpeapleSearchList(){
        DevartlinkAPI.getApis().getSearchPeaple().enqueue(new Callback<SearchPeapleResponse>() {
            @Override
            public void onResponse(Call<SearchPeapleResponse> call, Response<SearchPeapleResponse> response) {
                if (response.isSuccessful()){
                    peapleResponseMutableLiveData.postValue(response.body());
                }else {
                    peapleResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchPeapleResponse> call, Throwable t) {
                errorMessage.postValue(1);
            }
        });
    }
    public void getUserModel(String u,String p,String fcm){
        DevartlinkAPI.getApis().getModelUser(u,p,fcm).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()){
                    userResponseMutableLiveData.postValue(response.body());
                }else {
                    userResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                errorMessage.postValue(1);
            }
        });
    }
    public void getUserID(String userID){
        DevartlinkAPI.getApis().getUserID(UserPreferenceHelper.getUser()
                .getId(),userID).enqueue(new Callback<UserIDResponse>() {
            @Override
            public void onResponse(Call<UserIDResponse> call, Response<UserIDResponse> response) {
                if (response.isSuccessful()){
                    userIDResponseMutableLiveData.postValue(response.body());
                }else {
                    userIDResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserIDResponse> call, Throwable t) {
                errorMessage.postValue(1);
            }
        });
    }
}
