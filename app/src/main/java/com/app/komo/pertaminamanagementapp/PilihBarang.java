package com.app.komo.pertaminamanagementapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.komo.pertaminamanagementapp.API.ApiClient;
import com.app.komo.pertaminamanagementapp.API.ApiInterface;
import com.app.komo.pertaminamanagementapp.Object.Customer;
import com.app.komo.pertaminamanagementapp.Object.Order;
import com.app.komo.pertaminamanagementapp.Object.OrderDetail;
import com.google.android.gms.common.api.Api;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilihBarang extends AppCompatActivity {

    private Context context = this;
    private Button button_konfirmasi;
    private ApiInterface apiService;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_barang);
        getSupportActionBar().setTitle("Pilih Barang");

        apiService = ApiClient.getClient().create(ApiInterface.class);
        pref = getSharedPreferences("PERTAMINA", Context.MODE_PRIVATE);

        final Bundle extras = getIntent().getExtras();

        TextView button_tambah_stok = findViewById(R.id.button_tambah_stok);
        final TextView amount3 = findViewById(R.id.amount3);
        final TextView detailAmount3 = findViewById(R.id.detailAmount3);
        final TextView price3 = findViewById(R.id.price3);
        final TextView totalPrice = findViewById(R.id.totalPrice);
        button_tambah_stok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_tambah_stok);
                dialog.setCanceledOnTouchOutside(true);
                Button tambahButton = dialog.findViewById(R.id.tambahButton);
                final EditText amount = dialog.findViewById(R.id.amount);
                tambahButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        amount3.setText(amount.getText().toString());
                        detailAmount3.setText(amount.getText().toString() + "x");
                        price3.setText("Rp" + (Integer.parseInt(amount.getText().toString()) * 24000));
                        totalPrice.setText("Rp"+ (Integer.parseInt(amount.getText().toString()) * 24000));
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

        TextView button_tambah_stok2 = findViewById(R.id.button_tambah_stok2);
        final TextView amount5 = findViewById(R.id.amount5);
        final TextView detailAmount5 = findViewById(R.id.detailAmount5);
        final TextView price5 = findViewById(R.id.price5);
        button_tambah_stok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_tambah_stok);
                dialog.setCanceledOnTouchOutside(true);
                Button tambahButton = dialog.findViewById(R.id.tambahButton);
                final EditText amount = dialog.findViewById(R.id.amount);
                tambahButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        amount5.setText(amount.getText().toString());
                        detailAmount5.setText(amount.getText().toString() + "x");
                        price5.setText("Rp" + (Integer.parseInt(amount.getText().toString()) * 67000));
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        TextView button_tambah_stok3 = findViewById(R.id.button_tambah_stok3);
        final TextView amount12 = findViewById(R.id.amount12);
        final TextView detailAmount12 = findViewById(R.id.detailAmount5);
        final TextView price12 = findViewById(R.id.price12);
        button_tambah_stok3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_tambah_stok);
                dialog.setCanceledOnTouchOutside(true);
                Button tambahButton = dialog.findViewById(R.id.tambahButton);
                final EditText amount = dialog.findViewById(R.id.amount);
                tambahButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        amount12.setText(amount.getText().toString());
                        detailAmount12.setText(amount.getText().toString() + "x");
                        price12.setText("Rp" + (Integer.parseInt(amount.getText().toString()) * 155000));
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        button_konfirmasi = findViewById(R.id.button_konfirmasi);
        button_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = "Bearer " + pref.getString("token", "");
                OrderDetail orderDetail = new OrderDetail(
                        Integer.parseInt(amount3.getText().toString()),
                        Integer.parseInt(amount5.getText().toString()),
                        Integer.parseInt(amount12.getText().toString()));
                int total = Integer.parseInt(amount3.getText().toString()) * 24000 +
                        Integer.parseInt(amount5.getText().toString()) * 67000 +
                        Integer.parseInt(amount12.getText().toString()) * 155000;
                apiService.createTransaction(token,
                        new Order(new Customer(extras.getString("Name"),extras.getString("Address"), extras.getString("NIK"),
                                extras.getString("Pangkalan"), extras.getString("Phone")),extras.getString("Pangkalan"),
                                null, total, orderDetail))
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.d("Callback", ""+response.code());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
