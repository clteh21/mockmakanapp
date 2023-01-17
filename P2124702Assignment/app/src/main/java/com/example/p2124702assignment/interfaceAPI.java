package com.example.p2124702assignment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface interfaceAPI {
    @GET("hawker/data/all")
    Call<Data> getData();
}
