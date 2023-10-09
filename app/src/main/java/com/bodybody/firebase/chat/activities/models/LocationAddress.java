package com.bodybody.firebase.chat.activities.models;

import org.jetbrains.annotations.NotNull;

// 위치 정보를 담은 주소 클래스입니다.
// 유저 이름과 주소, 위도 경도 등을 나타냅니다.
public class LocationAddress {
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public LocationAddress(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @NotNull
    @Override
    public String toString() {
        return "LocationAddress{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

