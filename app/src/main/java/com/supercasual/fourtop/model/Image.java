package com.supercasual.fourtop.model;

public class Image {

    private int id;
    private String name;
    private String imageURL;
    private int rate;

    public Image() {
        this.id = 0;
        this.name = "";
        this.imageURL = "";
        this.rate = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
