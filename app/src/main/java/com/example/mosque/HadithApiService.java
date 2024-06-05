package com.example.mosque;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HadithApiService {
    @GET("daily.json")
    Call<HadithResponse> getDailyHadith();
}
