package com.example.proiect.network;

import com.example.proiect.models.ResearchThread;
import java.util.List;

public class ResearchExportResponse {
    private List<ResearchThread> threads;

    public List<ResearchThread> getThreads() {
        return threads;
    }

    public void setThreads(List<ResearchThread> threads) {
        this.threads = threads;
    }
}
