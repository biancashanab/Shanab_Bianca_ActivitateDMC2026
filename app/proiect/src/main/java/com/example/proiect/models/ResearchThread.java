package com.example.proiect.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ResearchThread implements Serializable {
    @SerializedName("thread_id")
    private String threadId;
    private String title;
    private String query;
    private String mode;
    private String planner;
    @SerializedName("updated_at")
    private String updatedAt;
    private java.util.List<PaperItem> papers;

    public ResearchThread() {}

    public ResearchThread(String threadId, String title, String query, String mode, String planner, String updatedAt, java.util.List<PaperItem> papers) {
        this.threadId = threadId;
        this.title = title;
        this.query = query;
        this.mode = mode;
        this.planner = planner;
        this.updatedAt = updatedAt;
        this.papers = papers;
    }

    public String getThreadId() { return threadId; }
    public void setThreadId(String threadId) { this.threadId = threadId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getPlanner() { return planner; }
    public void setPlanner(String planner) { this.planner = planner; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public java.util.List<PaperItem> getPapers() { return papers; }
    public void setPapers(java.util.List<PaperItem> papers) { this.papers = papers; }
}
