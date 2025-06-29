package com.example.siswaapp.API;

import com.example.siswaapp.Model.Siswa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SiswaApi {
    @GET("api/Siswa")
    Call<List<Siswa>> getAllSiswa();
    @POST("api/Siswa")
    Call<Siswa> createSiswa(@Body Siswa siswa);
    @PUT("api/Siswa/{id}")
    Call<Siswa> updateSiswa(@Path("id") int id, @Body Siswa siswa);
    @DELETE("api/Siswa/{id}")
    Call<Void> deleteSiswa(@Path("id") int id);
}
