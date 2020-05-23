package com.khhs.adminappbatch4;

public class SettingModel {
    String useCategoryReplace;
    String findByCategory;
    String useSlideShow;
    String slide1,slide2,slide3,slide4,slide5;

    public SettingModel(String useCategoryReplace, String findByCategory, String useSlideShow, String slide1, String slide2, String slide3, String slide4, String slide5) {
        this.useCategoryReplace = useCategoryReplace;
        this.findByCategory = findByCategory;
        this.useSlideShow = useSlideShow;
        this.slide1 = slide1;
        this.slide2 = slide2;
        this.slide3 = slide3;
        this.slide4 = slide4;
        this.slide5 = slide5;
    }

    public SettingModel() {
    }

    public String getUseCategoryReplace() {
        return useCategoryReplace;
    }

    public void setUseCategoryReplace(String useCategoryReplace) {
        this.useCategoryReplace = useCategoryReplace;
    }

    public String getFindByCategory() {
        return findByCategory;
    }

    public void setFindByCategory(String findByCategory) {
        this.findByCategory = findByCategory;
    }

    public String getUseSlideShow() {
        return useSlideShow;
    }

    public void setUseSlideShow(String useSlideShow) {
        this.useSlideShow = useSlideShow;
    }

    public String getSlide1() {
        return slide1;
    }

    public void setSlide1(String slide1) {
        this.slide1 = slide1;
    }

    public String getSlide2() {
        return slide2;
    }

    public void setSlide2(String slide2) {
        this.slide2 = slide2;
    }

    public String getSlide3() {
        return slide3;
    }

    public void setSlide3(String slide3) {
        this.slide3 = slide3;
    }

    public String getSlide4() {
        return slide4;
    }

    public void setSlide4(String slide4) {
        this.slide4 = slide4;
    }

    public String getSlide5() {
        return slide5;
    }

    public void setSlide5(String slide5) {
        this.slide5 = slide5;
    }
}
