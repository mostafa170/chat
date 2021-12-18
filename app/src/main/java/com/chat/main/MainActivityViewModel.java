package com.chat.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chat.main.model.searchPeople.SearchPeapleResponse;
import com.chat.network.DevartlinkAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {

    protected MutableLiveData<Integer> errorMessage;
    protected MutableLiveData<SearchPeapleResponse> peapleResponseMutableLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        peapleResponseMutableLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getErrorMessage() {
        return errorMessage;
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
}
