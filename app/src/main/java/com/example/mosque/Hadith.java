package com.example.mosque;

import com.google.gson.annotations.SerializedName;

public class Hadith {

    @SerializedName("collection")
    private String collection;

    @SerializedName("bookNumber")
    private String bookNumber;

    @SerializedName("hadithNumber")
    private String hadithNumber;

    @SerializedName("chapterId")
    private String chapterId;

    @SerializedName("chapterTitle")
    private String chapterTitle;

    @SerializedName("language")
    private String language;

    @SerializedName("narrator")
    private String narrator;

    @SerializedName("body")
    private String body;

    @SerializedName("grade")
    private String grade;

    public String getCollection() {
        return collection;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public String getHadithNumber() {
        return hadithNumber;
    }

    public String getChapterId() {
        return chapterId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public String getLanguage() {
        return language;
    }

    public String getNarrator() {
        return narrator;
    }

    public String getBody() {
        return body;
    }

    public String getGrade() {
        return grade;
    }
}
