package com.sa45team7.lussis.rest;

import com.sa45team7.lussis.rest.model.Employee;
import com.sa45team7.lussis.rest.model.Requisition;
import com.sa45team7.lussis.rest.model.Stationery;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by nhatton on 1/17/18.
 */

public interface LUSSISService {

    @GET("Stationeries/")
    Call<List<Stationery>> getAllStationeries();

    @FormUrlEncoded
    @POST("auth/Login")
    Call<Employee> login(@Field("Email") String email,
                         @Field("Password") String password);

    @GET("Requisitions/{dept}")
    Call<List<Requisition>> getPendingRequisitions(@Path("dept") String deptCode);
}
