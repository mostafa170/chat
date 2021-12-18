package com.chat.network;


import com.chat.main.model.searchPeople.SearchPeapleResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("search-users")
    Call<SearchPeapleResponse> getSearchPeaple();


}
