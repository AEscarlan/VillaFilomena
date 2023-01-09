package com.example.villafilomena.Guest.home_booking;

public class CottageInfos_model {
    private String id, imageUrl, name, cottage_capacity, cottage_rate;

    public CottageInfos_model(String id, String imageUrl, String name, String cottage_capacity, String cottage_rate) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.cottage_capacity = cottage_capacity;
        this.cottage_rate = cottage_rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCottage_capacity() {
        return cottage_capacity;
    }

    public void setCottage_capacity(String cottage_capacity) {
        this.cottage_capacity = cottage_capacity;
    }

    public String getCottage_rate() {
        return cottage_rate;
    }

    public void setCottage_rate(String cottage_rate) {
        this.cottage_rate = cottage_rate;
    }
}
