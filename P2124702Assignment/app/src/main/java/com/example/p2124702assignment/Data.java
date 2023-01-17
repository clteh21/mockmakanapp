package com.example.p2124702assignment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("stall_amount")
    private int stallAmount;
    @SerializedName("location")
    private String location;
    @SerializedName("status")
    private String status;
    @SerializedName("image")
    private String image;
    @SerializedName("crowd_status")
    private String crowdStatus;

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;

    }

    public Data(int id, String name, int stallAmount, String location, String status, String image, String crowdStatus) {
        this.id = id;
        this.name = name;
        this.stallAmount = stallAmount;
        this.location = location;
        this.status = status;
        this.image = image;
        this.crowdStatus = crowdStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStallAmount() {
        return stallAmount;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public String getCrowdStatus() {
        return crowdStatus;
    }
}

