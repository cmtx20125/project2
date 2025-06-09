package com.example.myapplication.bean;

import android.net.Uri;

public class VideoItem {
    private long id;
    private String name;
    private long duration;
    private long size;
    private Uri uri;
    private String path;

    public VideoItem(long id, String name, long duration, long size, Uri uri, String path) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.uri = uri;
        this.path = path;
    }

    // Getters
    public long getId() { return id; }
    public String getName() { return name; }
    public long getDuration() { return duration; }
    public long getSize() { return size; }
    public Uri getUri() { return uri; }
    public String getPath() { return path; }
}
