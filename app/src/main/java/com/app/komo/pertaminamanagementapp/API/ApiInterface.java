package com.app.komo.pertaminamanagementapp.API;

import com.app.komo.pertaminamanagementapp.CustomerResponse;
import com.app.komo.pertaminamanagementapp.Object.ApiResponse;
import com.app.komo.pertaminamanagementapp.Object.Customer;
import com.app.komo.pertaminamanagementapp.Object.LoginResponse;
import com.app.komo.pertaminamanagementapp.Object.Order;
import com.app.komo.pertaminamanagementapp.Object.PangkalanResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("order/")
    Call<ApiResponse<Order[]>> getTransaction(@Header("Authorization") String authorization,
                               @Query("pangkalan") String pangkalan,
                               @Query("from") Long from,
                               @Query("to") Long to
                                );
    @POST("order/")
    Call<String> createTransaction(@Header("Authorization") String authorization,
                                  @Body Order order);

    @FormUrlEncoded
    @POST("login/")
    Call<LoginResponse> login(@Field("username") String username,
                              @Field("password") String password,
                              @Field("role") String role);

    @GET("pangkalan/{id}")
    Call<ApiResponse<PangkalanResponse>> getPangkalan(@Header("Authorization") String authorization,
                                   @Path("id") String id);

    @GET("customer/")
    Call<ApiResponse<CustomerResponse>> getCustomer(@Header("Authorization") String authorization,
                                                    @Query("nik") String nik,
                                                    @Query("pangkalan") String pangkalan);

}
