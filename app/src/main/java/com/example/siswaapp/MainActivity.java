package com.example.siswaapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siswaapp.API.JurusanApi;
import com.example.siswaapp.API.MapelApi;
import com.example.siswaapp.API.SiswaApi;
import com.example.siswaapp.Adapter.SiswaAdapter;
import com.example.siswaapp.Model.Siswa;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SiswaApi siswaApi;
    private JurusanApi jurusanApi;
    private MapelApi mapelApi;
    private TextInputEditText etId, etNama, etKelas;
    private AutoCompleteTextView actJurusan,actMapel;
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etId = findViewById(R.id.et_id);
        etNama = findViewById(R.id.et_nama);
        etKelas = findViewById(R.id.et_kelas);
        actJurusan = findViewById(R.id.jurusan);
        actMapel = findViewById(R.id.mapel);
        btnSubmit = findViewById(R.id.btn_submit);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        siswaApi = ApiClient.getClient().create(SiswaApi.class);
        jurusanApi = ApiClient.getClient().create(JurusanApi.class);
        mapelApi = ApiClient.getClient().create(MapelApi.class);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etId.getText().toString();
                String nama = etNama.getText().toString();
                String kelas = etKelas.getText().toString();
                if (id.isEmpty() || nama.isEmpty() || kelas.isEmpty()){
                    Toast.makeText(MainActivity.this, "Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    Siswa siswa = new Siswa();
                    siswa.setId(Integer.parseInt(id));
                    siswa.setNama(nama);
                    siswa.setKelas(kelas);
                    siswa.setJurusan(actJurusan.getText().toString());
                    siswa.setMapel(actMapel.getText().toString());
                    Call<Siswa> call = siswaApi.createSiswa(siswa);
                    call.enqueue(new Callback<Siswa>() {
                        @Override
                        public void onResponse(Call<Siswa> call, Response<Siswa> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                getSiswa();
                            }else {
                                Log.e("RETROFIT", "Kode error: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Siswa> call, Throwable t) {
                            Log.e("RETROFIT", "Error : " + t.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSiswa();
        getJurusan();
        getMapel();
    }

    private void getMapel() {
        mapelApi.getAllMapel().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()){
                    List<String> mapelList = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            mapelList
                    );
                    actMapel.setAdapter(adapter);
                }else {
                    Log.e("RETROFIT", "Kode error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("RETROFIT", "Error : " + t.getMessage());
            }
        });
    }

    private void getJurusan() {
        jurusanApi.getAllJurusan().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()){
                    List<String> jurusanList = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            jurusanList
                    );
                    actJurusan.setAdapter(adapter);
                }else{
                    Log.e("RETROFIT", "Kode error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("RETROFIT", "Error : " + t.getMessage());
            }
        });
    }

    private void getSiswa() {
        Call<List<Siswa>> call = siswaApi.getAllSiswa();
        call.enqueue(new Callback<List<Siswa>>() {
            @Override
            public void onResponse(Call<List<Siswa>> call, Response<List<Siswa>> response) {
                if (response.isSuccessful()) {
                    List<Siswa> siswaList = response.body();
                    Log.d("RETROFIT", "Jumlah data: " + siswaList.size());

                    if (siswaList != null && !siswaList.isEmpty()) {
                        SiswaAdapter adapter = new SiswaAdapter(siswaList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(MainActivity.this, "Data kosong!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("RETROFIT", "Kode error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Siswa>> call, Throwable t) {
                Log.e("RETROFIT", "Error : " + t.getMessage());
            }
        });
    }
}