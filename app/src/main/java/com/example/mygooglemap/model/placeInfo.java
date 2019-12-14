package com.example.mygooglemap.model;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class placeInfo {

    private String name;
    private String address;
    private String phoneNumber;
    private String id;
    private Uri websitesUri;
    private LatLng latLng;
    private Float rating;
    private String attributions;

    public placeInfo(){}

    public placeInfo(String name, String address, String phoneNumber, String id, Uri websitesUri, LatLng latLng, Float rating, String attributions) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.websitesUri = websitesUri;
        this.latLng = latLng;
        this.rating = rating;
        this.attributions = attributions;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getWebsitesUri() {
        return websitesUri;
    }

    public void setWebsitesUri(Uri websitesUri) {
        this.websitesUri = websitesUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    @Override
    public String toString() {
        return "placeInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", websitesUri=" + websitesUri +
                ", latLng=" + latLng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                '}';
    }
}
