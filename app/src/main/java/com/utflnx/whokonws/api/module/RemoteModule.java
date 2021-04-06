package com.utflnx.whokonws.api.module;

import com.utflnx.whokonws.api.service.RemoteService;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.api.utils.ListObjects;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RemoteModule {
    public static Retrofit provideRetrofit(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(ListObjects.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static RemoteService provideMySqlService(){
        return provideRetrofit().create(RemoteService.class);
    }

    public static APIRequestModel passingRequestModel(String aboutName, String[] tableName, Object container, Object container_next_1, String param){
        APIRequestModel APIRequestModel = new APIRequestModel();
        APIRequestModel.setAbout(aboutName);
        APIRequestModel.setDatabase(ListObjects.DATABASE_NAME);

        HashMap<String, Object> table = new HashMap<>();
        table.put("name", tableName);
        if (param != null) table.put("param", param);
        if (container != null) table.put("container", container);
        if (container_next_1 != null) table.put("container_next_1", container_next_1);

        APIRequestModel.setTable(table);

        return APIRequestModel;
    }
}
