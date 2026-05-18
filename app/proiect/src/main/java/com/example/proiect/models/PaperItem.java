package com.example.proiect.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class PaperItem implements Serializable {
    private String id;
    @SerializedName("thread_id")
    private String threadId;
    private String title;
    private List<String> authors; 
    private int year;
    private String source;
    private String doi;
    private String url;
    @SerializedName("abstract")
    private String abstractText;
    @SerializedName("citation_count")
    private int citationCount;
    private String institution;
    private String country;
    private Double lat;
    private Double lng;
    @SerializedName("openalex_id")
    private String openAlexId;
    private float userRating;

    public PaperItem() {}
    
    public float getUserRating() { return userRating; }
    public void setUserRating(float userRating) { this.userRating = userRating; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getThreadId() { return threadId; }
    public void setThreadId(String threadId) { this.threadId = threadId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getAuthorsList() { return authors; }
    public void setAuthorsList(List<String> authors) { this.authors = authors; }

    public String getAuthors() {
        if (authors == null || authors.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            sb.append(authors.get(i));
            if (i < authors.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public void setAuthors(String authorsString) {
        if (authorsString == null || authorsString.isEmpty()) {
            this.authors = null;
            return;
        }
        this.authors = java.util.Arrays.asList(authorsString.split(", "));
    }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDoi() { return doi; }
    public void setDoi(String doi) { this.doi = doi; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public int getCitationCount() { return citationCount; }
    public void setCitationCount(int citationCount) { this.citationCount = citationCount; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }

    public String getOpenAlexId() { return openAlexId; }
    public void setOpenAlexId(String openAlexId) { this.openAlexId = openAlexId; }

    public String getBestAvailableUrl() {
        if (doi != null && !doi.isEmpty()) {
            if (doi.startsWith("http")) return doi;
            return "https://doi.org/" + doi;
        }
        if (url != null && !url.isEmpty()) return url;
        if (openAlexId != null && !openAlexId.isEmpty()) return openAlexId;
        return null;
    }
}
