package com.ptnst.neon.neon.model;

import com.ptnst.neon.neon.data.TextData;

import java.io.Serializable;

public class FontData extends TextData implements Serializable{
    public String lang;
    public String name;
    public String file;
    public String path;
    public String icon;
    public String preview;
    public int purchase;
    public String download_url;
    public int need_download;
    public String sku;
}
