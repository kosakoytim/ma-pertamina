package com.app.komo.pertaminamanagementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Pembelian extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian);
        getSupportActionBar().setTitle("Pembelian");

        LinearLayout no_telepon_layout = (LinearLayout) findViewById(R.id.no_telepon_layout);
        LinearLayout nama_layout = (LinearLayout) findViewById(R.id.nama_layout);
        LinearLayout no_ktp_layout = (LinearLayout) findViewById(R.id.no_ktp_layout);
        LinearLayout alamat_layout = (LinearLayout) findViewById(R.id.alamat_layout);
        Button button_konfirmasi = (Button) findViewById(R.id.button_konfirmasi);
        final EditText no_ktp = (EditText) findViewById(R.id.no_ktp);
        final EditText nama = (EditText) findViewById(R.id.nama);
        final EditText no_telepon = (EditText) findViewById(R.id.no_telepon);
        final EditText alamat = (EditText) findViewById(R.id.alamat);

        no_telepon_layout.setVisibility(View.VISIBLE);
        nama_layout.setVisibility(View.VISIBLE);
        no_ktp_layout.setVisibility(View.VISIBLE);
        alamat_layout.setVisibility(View.VISIBLE);
        button_konfirmasi.setVisibility(View.VISIBLE);

        final Bundle extras = getIntent().getExtras();

        if (!extras.getString("NIK").equals("")) {
            no_ktp.setText(extras.getString("NIK"));
        }

        if (extras.getString("Name") != null) {
            alamat.setText(extras.getString("Address"));
            nama.setText(extras.getString("Name"));
            no_telepon.setText(extras.getString("Phone"));
        }

        button_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PilihBarang.class);
                intent.putExtra("Name",nama.getText().toString());
                intent.putExtra("Address",alamat.getText().toString());
                intent.putExtra("NIK",no_ktp.getText().toString());
                intent.putExtra("Phone",no_telepon.getText().toString());
                intent.putExtra("Pangkalan",extras.getString("Pangkalan"));
                startActivity(intent);
            }
        });
    }
}
