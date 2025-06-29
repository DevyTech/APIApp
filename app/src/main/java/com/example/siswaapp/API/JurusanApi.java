package com.example.siswaapp.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JurusanApi {
    @GET("api/Jurusan")
    Call<List<String>> getAllJurusan();
}
