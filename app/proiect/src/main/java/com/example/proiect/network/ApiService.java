package com.example.proiect.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    Call<ResearchExportResponse> getResearchResults(@Url String url);
}
