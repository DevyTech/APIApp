package com.example.siswaapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siswaapp.API.SiswaApi;
import com.example.siswaapp.ApiClient;
import com.example.siswaapp.Model.Siswa;
import com.example.siswaapp.R;
import com.example.siswaapp.UpdateActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiswaAdapter extends RecyclerView.Adapter<SiswaAdapter.ViewHolder> {
    private List<Siswa> siswaList;
    private SiswaApi siswaApi;

    public SiswaAdapter(List<Siswa> siswaList) {
        this.siswaList = siswaList;
        siswaApi = ApiClient.getClient().create(SiswaApi.class);
    }

    @NonNull
    @Override
    public SiswaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_siswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiswaAdapter.ViewHolder holder, int position) {
        Siswa siswa = siswaList.get(position);
        holder.nama.setText(siswa.getNama());
        holder.kelas.setText(siswa.getKelas());
        holder.jurusan.setText(siswa.getJurusan());
        holder.mapel.setText(siswa.getMapel());
        holder.menu.setOnClickListener(v -> showPopupMenu(v, position));
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, kelas, jurusan, mapel;
        ImageView menu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.textNama);
            kelas = itemView.findViewById(R.id.textKelas);
            jurusan = itemView.findViewById(R.id.textJurusan);
            mapel = itemView.findViewById(R.id.textMapel);
            menu = itemView.findViewById(R.id.menu);
        }
    }
    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.update){
                    Intent intent = new Intent(view.getContext(), UpdateActivity.class);
                    intent.putExtra("id", String.valueOf(siswaList.get(position).getId()));
                    intent.putExtra("nama", siswaList.get(position).getNama());
                    intent.putExtra("kelas", siswaList.get(position).getKelas());
                    intent.putExtra("jurusan", siswaList.get(position).getJurusan());
                    intent.putExtra("mapel", siswaList.get(position).getMapel());
                    view.getContext().startActivity(intent);
                    return true;
                } else if (itemId == R.id.delete) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Delete Data")
                            .setMessage("Are you sure to delete this data ?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                siswaApi.deleteSiswa(siswaList.get(position).getId()).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()){
                                            siswaList.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, siswaList.size());
                                            notifyDataSetChanged();
                                            Toast.makeText(view.getContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Log.e("RETROFIT", "Kode error: " + response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e("RETROFIT", "Error : " + t.getMessage());
                                    }
                                });
                            })
                            .setNegativeButton("Cancel",null)
                            .show();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
