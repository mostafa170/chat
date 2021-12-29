package com.chat.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chat.home.model.PeopleListResponse;
import com.chat.main.model.searchPeople.SearchPeapleResponse;
import com.chat.main.model.user.UserResponse;
import com.chat.network.DevartlinkAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    protected MutableLiveData<Integer> errorMessage;
    protected MutableLiveData<PeopleListResponse> peapleResponseMutableLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        peapleResponseMutableLiveData = new MutableLiveData<>();
        peapleResponseMutableLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<PeopleListResponse> getPeapleResponseMutableLiveData() {
        return peapleResponseMutableLiveData;
    }

    public void getListPeaple(String id){
        DevartlinkAPI.getApis().getPeopleList(id).enqueue(new Callback<PeopleListResponse>() {
            @Override
            public void onResponse(Call<PeopleListResponse> call, Response<PeopleListResponse> response) {
                if (response.isSuccessful()){
                    peapleResponseMutableLiveData.postValue(response.body());
                }else {
                    peapleResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<PeopleListResponse> call, Throwable t) {
                errorMessage.postValue(1);
            }
        });
    }
}
