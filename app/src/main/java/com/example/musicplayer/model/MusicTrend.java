package com.example.musicplayer.model;

public class MusicTrend {
    private String id;
    private long idSong;

    public MusicTrend(String id, long idSong) {
        this.id = id;
        this.idSong = idSong;
    }

    public long getIdSong() {
        return idSong;
    }

    public void setIdSong(long idSong) {
        this.idSong = idSong;
    }

    public MusicTrend() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
