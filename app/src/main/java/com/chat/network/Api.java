package com.chat.network;


import com.chat.home.model.PeopleListResponse;
import com.chat.main.model.searchPeople.SearchPeapleResponse;
import com.chat.main.model.user.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("search-users")
    Call<SearchPeapleResponse> getSearchPeaple();

    @GET("crm-chat")
    Call<UserResponse> getModelUser(@Query("u") String userName,
                                    @Query("p") String password,
                                    @Query("fcm") String fcm);

    @GET("people_list")
    Call<PeopleListResponse> getPeopleList(@Query("id") String id);


}
