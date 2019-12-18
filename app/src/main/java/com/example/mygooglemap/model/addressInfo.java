package com.example.mygooglemap.model;

public class addressInfo {

    private double latitude;
    private double longitude;
    private String contry;
    private String locality;
    private String postalcode;
    private String  url;
    private String phoneNumber;

    public addressInfo(){

    }

    public addressInfo(double latitude, double longitude, String contry, String locality, String postalcode, String url, String phoneNumber) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.contry = contry;
        this.locality = locality;
        this.postalcode = postalcode;
        this.url = url;
        this.phoneNumber = phoneNumber;
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

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
