package com.example.mosque;

import com.google.gson.annotations.SerializedName;

    public class SalatResponse {
        @SerializedName("data")
        private SalatData data;

        public SalatData getData() {
            return data;
        }
    }

    class SalatData {
        @SerializedName("timings")
        private SalatTimings timings;

        public SalatTimings getTimings() {
            return timings;
        }
    }

    class SalatTimings {
        @SerializedName("Fajr")
        private String fajr;

        @SerializedName("Dhuhr")
        private String dhuhr;

        @SerializedName("Asr")
        private String asr;

        @SerializedName("Maghrib")
        private String maghrib;

        @SerializedName("Isha")
        private String isha;

        public String getFajr() {
            return fajr;
        }

        public String getDhuhr() {
            return dhuhr;
        }

        public String getAsr() {
            return asr;
        }

        public String getMaghrib() {
            return maghrib;
        }

        public String getIsha() {
            return isha;
        }
    }


