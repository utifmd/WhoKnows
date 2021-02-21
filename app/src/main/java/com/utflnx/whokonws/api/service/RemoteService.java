package com.utflnx.whokonws.api.service;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.api.utils.ListObjects;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RemoteService {

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<List<UserModel>> getAllUsers(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<String> postUser(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<String> deleteUser(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<String> updateUser(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<List<UserModel>> getUser(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<List<RoomModel>> getOwnerRoom(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<String> postOwnerRoom(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<String> updateOwnerRoom(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<String> deleteOwnerRoom(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<String> postQuizRoom(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<List<RoomModel>> getRoomById(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<List<QuizModel>> getRoomQuizList(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<String> postParticipant(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<List<ParticipantModel>> getParticipantBy(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<String> updateParticipant(@Body APIRequestModel APIRequestModel);

    @POST(ListObjects.PARAM_WHO_KNOWS_URL)
    Call<List<ResultModel>> getResultBy(@Body APIRequestModel APIRequestModel);
}
