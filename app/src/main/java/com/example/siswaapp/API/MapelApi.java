package com.example.siswaapp.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MapelApi {
    @GET("api/Mapel")
    Call<List<String>> getAllMapel();
}
