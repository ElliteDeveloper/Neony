package com.ptnst.neon.neon.model;

import java.util.List;

public class UnsplashResultData {

    public List<ThemePhotoData> results;
    public int total;
    public int total_pages;

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int i) {
        this.total = i;
    }

    public int getTotal_pages() {
        return this.total_pages;
    }

    public void setTotal_pages(int i) {
        this.total_pages = i;
    }

    public List<ThemePhotoData> getResults() {
        return this.results;
    }

    public void setResults(List<ThemePhotoData> list) {
        this.results = list;
    }


}
