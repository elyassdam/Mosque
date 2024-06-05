package com.example.mosque;

import com.google.gson.annotations.SerializedName;

public class HadithResponse {
    @SerializedName("data")
    private Hadith data;

    public Hadith getData() {
        return data;
    }

    public void setData(Hadith data) {
        this.data = data;
    }
}
