package com.example.siswaapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.siswaapp.API.SiswaApi;
import com.example.siswaapp.Model.Siswa;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {
    private SiswaApi siswaApi;
    private TextInputEditText etNama, etKelas;
    private AutoCompleteTextView actJurusan, actMapel;
    private Button btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        siswaApi = ApiClient.getClient().create(SiswaApi.class);

        etNama = findViewById(R.id.et_nama_update);
        etKelas = findViewById(R.id.et_kelas_update);
        actJurusan = findViewById(R.id.jurusan_update);
        actMapel = findViewById(R.id.mapel_update);
        btnUpdate = findViewById(R.id.btn_update);

        // Mengambil data siswa dari Intent
        String id = getIntent().getStringExtra("id");
        String nama = getIntent().getStringExtra("nama");
        String kelas = getIntent().getStringExtra("kelas");
        String jurusan = getIntent().getStringExtra("jurusan");
        String mapel = getIntent().getStringExtra("mapel");

        // Menampilkan data siswa di EditText
        etNama.setText(nama);
        etKelas.setText(kelas);
        actJurusan.setText(jurusan);
        actMapel.setText(mapel);

        Toast.makeText(this, "ID : "+id, Toast.LENGTH_SHORT).show();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Siswa siswa = new Siswa();
                siswa.setNama(etNama.getText().toString());
                siswa.setKelas(etKelas.getText().toString());
                siswa.setJurusan(actJurusan.getText().toString());
                siswa.setMapel(actMapel.getText().toString());
                Call<Siswa> call = siswaApi.updateSiswa(Integer.parseInt(id), siswa);
                call.enqueue(new Callback<Siswa>() {
                    @Override
                    public void onResponse(Call<Siswa> call, Response<Siswa> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(UpdateActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                            finish();
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
        });
    }
}