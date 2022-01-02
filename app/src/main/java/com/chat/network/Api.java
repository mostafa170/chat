package com.chat.network;


import com.chat.ChatThread.model.ChatListResponse;
import com.chat.ChatThread.model.sendMessages.SendMessagesResponse;
import com.chat.home.model.ImageModel.ImageProfileResponse;
import com.chat.home.model.mareSeen.MarkSeenRequest;
import com.chat.home.model.mareSeen.MarkSeenResponse;
import com.chat.home.model.peopleList.PeopleListResponse;
import com.chat.main.model.searchPeople.SearchPeapleResponse;
import com.chat.main.model.user.UserResponse;
import com.chat.main.model.userID.UserIDResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @GET("fetchmessages")
    Call<ChatListResponse> getChatList(@Query("id") String id);

    @GET("get-chat")
    Call<UserIDResponse> getUserID(@Query("id") String id,
                                   @Query("user_id") String user_id);

    @GET("image")
    Call<ImageProfileResponse> getImageProfile(@Query("id") String user_id);

    @Multipart
    @POST("sendmessage")
    Call<SendMessagesResponse> SEND_MESSAGES(@Part MultipartBody.Part file,
                                             @PartMap Map<String, RequestBody> send);

    @POST("makeSeen")
    Call<MarkSeenResponse> MARK_SEEN(@Body MarkSeenRequest markSeenRequest);


}
