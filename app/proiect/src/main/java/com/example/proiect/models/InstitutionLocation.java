package com.example.proiect.models;

public class InstitutionLocation {
    private String name;
    private String country;
    private double lat;
    private double lng;
    private String paperTitle;

    public InstitutionLocation() {}

    public InstitutionLocation(String name, String country, double lat, double lng, String paperTitle) {
        this.name = name;
        this.country = country;
        this.lat = lat;
        this.lng = lng;
        this.paperTitle = paperTitle;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public String getPaperTitle() { return paperTitle; }
    public void setPaperTitle(String paperTitle) { this.paperTitle = paperTitle; }
}
