package com.khhs.adminappbatch4;

public class EpisodeModel {

    public String epTitle;
    public String epVideo;
    public String epSeires;

    public String createdAt;

    public EpisodeModel(String epTitle, String epVideo, String epSeires, String createdAt) {
        this.epTitle = epTitle;
        this.epVideo = epVideo;
        this.epSeires = epSeires;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public EpisodeModel(String epTitle, String epVideo, String epSeires) {
        this.epTitle = epTitle;
        this.epVideo = epVideo;
        this.epSeires = epSeires;
    }

    public String getEpTitle() {
        return epTitle;
    }

    public EpisodeModel() {
    }

    public void setEpTitle(String epTitle) {
        this.epTitle = epTitle;
    }

    public String getEpVideo() {
        return epVideo;
    }

    public void setEpVideo(String epVideo) {
        this.epVideo = epVideo;
    }

    public String getEpSeires() {
        return epSeires;
    }

    public void setEpSeires(String epSeires) {
        this.epSeires = epSeires;
    }
}
