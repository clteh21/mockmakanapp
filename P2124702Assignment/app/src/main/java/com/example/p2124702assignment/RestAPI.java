package com.example.p2124702assignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RestAPI {
    private static Retrofit retrofit;
    private static String baseUrl = "http://10.0.2.2:8002/api/";

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(logging)
//                .build();

//        retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
////                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();


//    public void getData(final RestAPIListener listener) {
//        Api api = retrofit.create(Api.class);
//        Call<List<Data>> call = api.getData();
//        call.enqueue(new Callback<List<Data>>() {
//            @Override
//            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
//                if (response.isSuccessful()) {
//                    listener.onSuccess(response.body());
//                } else {
//                    listener.onError(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Data>> call, Throwable t) {
//                listener.onFailure(t.getMessage());
//            }
//        });
//    }

//    public interface Api {
//        @GET("hawker/data/all")
//        Call<List<Data>> getData();
//    }
//
//    public interface RestAPIListener {
//        void onSuccess(List<Data> data);
//
//        void onError(String message);
//
//        void onFailure(String message);
//    }
}