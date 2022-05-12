package com.ptnst.neon.neon.model;

import java.util.ArrayList;
import java.util.List;

public class AlbumData {
    public String albumId;
    public String albumName;
    public String image;
    public List<String> listPhotos;

    public AlbumData(){
        listPhotos = new ArrayList<>();
    }
}
