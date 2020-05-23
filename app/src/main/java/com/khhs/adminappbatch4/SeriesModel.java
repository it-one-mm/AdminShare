package com.khhs.adminappbatch4;

public class SeriesModel {

    public String seriesTitle;
   public String seriesImage;
    public String seriesCategory;

    public int seriesCount;

    public SeriesModel(String seriesTitle, String seriesImage, String seriesCategory, int seriesCount, String createdAt) {
        this.seriesTitle = seriesTitle;
        this.seriesImage = seriesImage;
        this.seriesCategory = seriesCategory;
        this.seriesCount = seriesCount;
        this.createdAt = createdAt;
    }

    public String createdAt;

    public SeriesModel(String seriesTitle, String seriesImage, String seriesCategory, String createdAt) {
        this.seriesTitle = seriesTitle;
        this.seriesImage = seriesImage;
        this.seriesCategory = seriesCategory;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public SeriesModel(String seriesTitle, String seriesImage, String seriesCategory) {
        this.seriesTitle = seriesTitle;
        this.seriesImage = seriesImage;
        this.seriesCategory = seriesCategory;
    }

    public SeriesModel() {
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public void setSeriesTitle(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public String getSeriesImage() {
        return seriesImage;
    }

    public void setSeriesImage(String seriesImage) {
        this.seriesImage = seriesImage;
    }

    public String getSeriesCategory() {
        return seriesCategory;
    }

    public void setSeriesCategory(String seriesCategory) {
        this.seriesCategory = seriesCategory;
    }
}


