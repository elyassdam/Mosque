package com.example.mosque;

import com.example.mosque.SalatResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AlAdhanApiService {
    @GET("v1/timingsByCity")
    Call<SalatResponse> getSalatTimingsByCity(
            @Query("pueblo")String pueblo,
            @Query("city") String city,
            @Query("country") String country,
            @Query("method") int method // Método de cálculo (por ejemplo, 2 para la Unión Islámica)
    );
}
