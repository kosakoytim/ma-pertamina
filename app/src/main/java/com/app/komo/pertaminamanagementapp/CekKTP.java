package com.app.komo.pertaminamanagementapp;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.komo.pertaminamanagementapp.API.ApiClient;
import com.app.komo.pertaminamanagementapp.API.ApiInterface;
import com.app.komo.pertaminamanagementapp.Object.ApiResponse;
import com.app.komo.pertaminamanagementapp.Object.PangkalanResponse;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CekKTP extends AppCompatActivity {

    CameraSource mCameraSource;
    SurfaceView mCameraView;
    TextView mTextView;
    private static final String TAG = "MainActivity";
    private static final int requestPermissionID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_ktp);
        getSupportActionBar().setTitle("Scan KTP");
        mCameraView = findViewById(R.id.surfaceView);
        startCameraSource();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Pembelian.class);
                intent.putExtra("NIK", "");
                startActivity(intent);
            }
        });

    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(CekKTP.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */

                ApiInterface apiService;

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i=0;i<items.size();i++){
                            final TextBlock item = items.valueAt(i);
                            stringBuilder.append(item.getValue());
                            stringBuilder.append("\n");

                            StringBuilder sb = new StringBuilder();
                            boolean found = false;
                            for(char c : item.getValue().toCharArray()){
                                if(!Character.isDigit(c)){
                                    found = true;
                                }
                            }

                            Log.d("get value", item.getValue());

//                            adalah nik
                            if(item.getValue().length() == 16 && !found) {
                                Log.d("get value found", item.getValue());
//                                Toast.makeText(getApplicationContext(), "NIK ditemukan",
//                                        Toast.LENGTH_LONG).show();

                                final SharedPreferences pref = getSharedPreferences("PERTAMINA", Context.MODE_PRIVATE);
                                final String token = "Bearer " + pref.getString("token","");
                                apiService = ApiClient.getClient().create(ApiInterface.class);

                                apiService.getPangkalan(token,pref.getString("username","")).enqueue(new Callback<ApiResponse<PangkalanResponse>>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse<PangkalanResponse>> call, Response<ApiResponse<PangkalanResponse>> response) {
                                        apiService.getCustomer(token, item.getValue(), response.body().getResults().getUsername()).enqueue(new Callback<ApiResponse<CustomerResponse>>() {
                                            @Override
                                            public void onResponse(Call<ApiResponse<CustomerResponse>> call, Response<ApiResponse<CustomerResponse>> response) {
//                                                mCameraSource.release();
                                                Intent intent = new Intent(getApplicationContext(), Pembelian.class);
                                                intent.putExtra("NIK",item.getValue());
                                                if (response.code() == 200) {
                                                    intent.putExtra("Name",response.body().getResults().getName());
                                                    intent.putExtra("Address",response.body().getResults().getAddress());
                                                    intent.putExtra("Phone",response.body().getResults().getPhone());
                                                    intent.putExtra("Id",response.body().getResults().getId());
//                                                    Toast.makeText(getApplicationContext(), "NIK sudah terdaftar",
//                                                            Toast.LENGTH_LONG).show();
                                                } else {
//                                                    Toast.makeText(getApplicationContext(), "NIK belum terdaftar",
//                                                            Toast.LENGTH_LONG).show();
                                                }
                                                startActivity(intent);

                                            }

                                            @Override
                                            public void onFailure(Call<ApiResponse<CustomerResponse>> call, Throwable t) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse<PangkalanResponse>> call, Throwable t) {

                                    }
                                });



                            }
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraSource.start(mCameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
