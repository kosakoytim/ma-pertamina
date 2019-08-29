package com.app.komo.pertaminamanagementapp;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.microblink.activity.ScanCard;
import com.microblink.hardware.camera.CameraType;
import com.microblink.recognition.InvalidLicenceKeyException;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkid.indonesia.front.IndonesianIDFrontRecognitionResult;
import com.microblink.recognizers.blinkid.indonesia.front.IndonesianIDFrontRecognizerSettings;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.recognizers.settings.RecognizerSettingsUtils;
import com.microblink.util.RecognizerCompatibility;
import com.microblink.view.CameraAspectMode;
import com.microblink.view.recognition.RecognizerView;

import com.app.komo.pertaminamanagementapp.API.ApiClient;
import com.app.komo.pertaminamanagementapp.API.ApiInterface;
import com.app.komo.pertaminamanagementapp.Object.ApiResponse;
import com.app.komo.pertaminamanagementapp.Object.Customer;
import com.app.komo.pertaminamanagementapp.Object.Order;
import com.app.komo.pertaminamanagementapp.Object.OrderDetail;
import com.app.komo.pertaminamanagementapp.Object.PangkalanResponse;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private LinearLayout content_pembelian;
    private LinearLayout content_stok;
    private LinearLayout content_profil;
    private Context context = this;
    private ApiInterface apiService;

    private Long getToDate;
    private Long getFromDate;

    private List<Order> transaksiList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterTransaksi adapterTransaksi;
    private SharedPreferences pref;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_pembelian:
                    content_pembelian = findViewById(R.id.content_pembelian);
                    content_stok = findViewById(R.id.content_stok);
                    content_profil = findViewById(R.id.content_profil);
                    content_pembelian.setVisibility(View.VISIBLE);
                    content_stok.setVisibility(View.GONE);
                    content_profil.setVisibility(View.GONE);
                    getSupportActionBar().setTitle("Daftar Transaksi");
                    return true;
//                case R.id.navigation_stok:
//                    content_pembelian = (LinearLayout)findViewById(R.id.content_pembelian);
//                    content_stok = (LinearLayout)findViewById(R.id.content_stok);
//                    content_profil = (LinearLayout)findViewById(R.id.content_profil);
//                    content_pembelian.setVisibility(View.GONE);
//                    content_stok.setVisibility(View.VISIBLE);
//                    content_profil.setVisibility(View.GONE);
//                    getSupportActionBar().setTitle("Stok");
//                    return true;
                case R.id.navigation_profil:
                    content_pembelian = findViewById(R.id.content_pembelian);
                    content_stok = findViewById(R.id.content_stok);
                    content_profil = findViewById(R.id.content_profil);
                    content_pembelian.setVisibility(View.GONE);
                    content_stok.setVisibility(View.GONE);
                    content_profil.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle("Profil");
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("PERTAMINA", Context.MODE_PRIVATE);
        if (pref.getString("token","").equals("") || pref.getString("username","").equals("")) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        } else {
            apiService = ApiClient.getClient().create(ApiInterface.class);

            Long time = System.currentTimeMillis();
            getFromDate = startOfDay(time);
            getToDate = (getFromDate + 86399999);

            // First Create
            content_pembelian = findViewById(R.id.content_pembelian);
            content_stok = findViewById(R.id.content_stok);
            content_profil = findViewById(R.id.content_profil);
            content_pembelian.setVisibility(View.VISIBLE);
            content_stok.setVisibility(View.GONE);
            content_profil.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Daftar Transaksi");
            getSupportActionBar().setElevation(0);

            getDataPembelian();

            setupStok();
            setupProfil();

            BottomNavigationView navigation = findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }
    }

    private RecognizerView mRecognizerView;

    private RecognizerSettings[] setupSettingsArray() {
        IndonesianIDFrontRecognizerSettings sett = new IndonesianIDFrontRecognizerSettings();

        // now add sett to recognizer settings array that is used to configure
        // recognition
        return new RecognizerSettings[] { sett };
    }


    EditText edittext;
    Calendar myCalendar;
    TextView mTextView;

    private int MY_REQUEST_CODE=578;


    private final int CAMERA_SCAN_TEXT = 0;
    private final int LOAD_IMAGE_RESULTS = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == ScanCard.RESULT_OK && data != null) {
                // perform processing of the data here

                // for example, obtain parcelable recognition result
                Bundle extras = data.getExtras();
                RecognitionResults results = data.getParcelableExtra(ScanCard.EXTRAS_RECOGNITION_RESULTS);

                // get array of recognition results
                BaseRecognitionResult[] dataArray = results.getRecognitionResults();
                for(BaseRecognitionResult baseResult : dataArray) {
                    if(baseResult instanceof IndonesianIDFrontRecognitionResult) {
                        IndonesianIDFrontRecognitionResult result = (IndonesianIDFrontRecognitionResult) baseResult;

                        // you can use getters of IndonesianIDFrontRecognitionResult class to
                        // obtain scanned information
                        if(!result.isEmpty()) {
                            final String name = result.getName();
                            final String address = result.getAddress();
                            final String nik = result.getDocumentNumber();

                            final SharedPreferences pref = getSharedPreferences("PERTAMINA", Context.MODE_PRIVATE);
                            final String token = "Bearer " + pref.getString("token","");
                            apiService = ApiClient.getClient().create(ApiInterface.class);

                            apiService.getPangkalan(token,pref.getString("username","")).enqueue(new Callback<ApiResponse<PangkalanResponse>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<PangkalanResponse>> call, Response<ApiResponse<PangkalanResponse>> response) {
                                    final String username_pangkalan = response.body().getResults().getUsername();
                                    apiService.getCustomer(token, nik, response.body().getResults().getUsername()).enqueue(new Callback<ApiResponse<CustomerResponse>>() {
                                        @Override
                                        public void onResponse(Call<ApiResponse<CustomerResponse>> call, Response<ApiResponse<CustomerResponse>> response) {
//                                                mCameraSource.release();
                                            Intent intent = new Intent(getApplicationContext(), Pembelian.class);
                                            intent.putExtra("NIK",nik);
                                            if (response.code() == 200) {
//                                                intent.putExtra("Name",response.body().getResults().getName());
//                                                intent.putExtra("Address",response.body().getResults().getAddress());
                                                intent.putExtra("Phone",response.body().getResults().getPhone());
                                                intent.putExtra("Id",response.body().getResults().getId());
//                                                intent.putExtra("Pangkalan",username_pangkalan);
                                                intent.putExtra("Pangkalan",username_pangkalan);
                                                intent.putExtra("Address",address);
                                                intent.putExtra("Name",name);
                                            } else {
                                                intent.putExtra("Pangkalan",username_pangkalan);
                                                intent.putExtra("Address",address);
                                                intent.putExtra("Name",name);
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
                        } else {
                            // not all relevant data was scanned, ask user
                            // to try again
                        }
                    }
                }
            }
        }
    }


    private void setupPembelian() {
        final SharedPreferences pref = getSharedPreferences("PERTAMINA", Context.MODE_PRIVATE);

        edittext= findViewById(R.id.tanggal_pembelian);
        myCalendar = Calendar.getInstance();

        TextView button_pembelian = findViewById(R.id.button_pembelian);
        button_pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Intent intent = new Intent(getApplicationContext(), CekKTP.class);
//                startActivity(intent);

                // Intent for ScanCard Activity
                Intent intent = new Intent(getApplicationContext(), ScanCard.class);

// set your licence key
// obtain your licence key at http://microblink.com/login or
// contact us at http://help.microblink.com
                intent.putExtra(ScanCard.EXTRAS_LICENSE_KEY, "Y5RWPJNT-XPEEVF6P-BXYBJ45D-GOG2MBPE-MWRGSNK3-7ARUHIKZ-3DWADHR7-WQFTO3K3");

                RecognitionSettings settings = new RecognitionSettings();
// setup array of recognition settings (described in chapter "Recognition
// settings and results")
                settings.setRecognizerSettingsArray(setupSettingsArray());
                intent.putExtra(ScanCard.EXTRAS_RECOGNITION_SETTINGS, settings);

// Starting Activity
                startActivityForResult(intent, MY_REQUEST_CODE);

            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                getFromDate = myCalendar.getTimeInMillis();
                getToDate = (getFromDate + 86399999);
                updateLabel();
                transaksiList.clear();
                adapterTransaksi.notifyDataSetChanged();
                getDataPembelian();
            }
        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            // TODO Auto-generated method stub
            DatePickerDialog datePicker = new DatePickerDialog(MainActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);

        adapterTransaksi = new AdapterTransaksi(transaksiList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterTransaksi);
        adapterTransaksi.notifyDataSetChanged();
    }

    private void updateLabel() {
//        myCalendar = Calendar.getInstance();
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    private void setupProfil() {
        final SharedPreferences pref = getSharedPreferences("PERTAMINA", Context.MODE_PRIVATE);
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // We need an editor object to make changes
                SharedPreferences.Editor edit = pref.edit();

                // Set/Store data
                edit.remove("token");
                edit.remove("username");
                edit.commit();

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    private void setupStok() {
        TextView button_tambah_stok = findViewById(R.id.button_tambah_stok);
        button_tambah_stok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_tambah_stok);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();


            }
        });

        TextView button_tambah_stok2 = findViewById(R.id.button_tambah_stok2);
        button_tambah_stok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_tambah_stok);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();


            }
        });

        TextView button_tambah_stok3 = findViewById(R.id.button_tambah_stok3);
        button_tambah_stok3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_tambah_stok);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();


            }
        });
    }

    private void getDataPembelian(){
        final TextView nama_user = findViewById(R.id.nama_user);
        final TextView pangkalan = findViewById(R.id.pangkalan);

        final String token = "Bearer " + pref.getString("token","");
        apiService.getPangkalan(token,pref.getString("username","")).enqueue(new Callback<ApiResponse<PangkalanResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PangkalanResponse>> call, Response<ApiResponse<PangkalanResponse>> response) {
                nama_user.setText(response.body().getResults().getOwner());
                pangkalan.setText(response.body().getResults().getName());

                apiService.getTransaction(token, response.body().getResults().getUsername(),getFromDate,getToDate).enqueue(new Callback<ApiResponse<Order[]>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Order[]>> call, Response<ApiResponse<Order[]>> response) {

                        Order [] orders= response.body().getResults();
                        for(int i=0; i< orders.length; i++) {
                            transaksiList.add(orders[i]);
                        }
                        Collections.reverse(transaksiList);
                        setupPembelian();

                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Order[]>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<PangkalanResponse>> call, Throwable t) {

            }
        });
    }

    public static Long startOfDay(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 0); //set hours to zero
        cal.set(Calendar.MINUTE, 0); // set minutes to zero
        cal.set(Calendar.SECOND, 0); //set seconds to zero
        Log.i("Time", cal.getTime().toString());
        return cal.getTimeInMillis();
    }
}
