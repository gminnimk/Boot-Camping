package com.sparta.studytrek.summary.service;

import java.util.Optional;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {
    @Value("${NAVER_CLOVA_CLIENT_ID}")
    private String clientId;

    @Value("${NAVER_CLOVA_CLIENT_SECRET}")
    private String clientSecret;

    private final String apiURL = "POST https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";

    public Optional<String> summarizeText(String text) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("document", new JSONObject().put("content", text));
        requestBody.put("option", new JSONObject().put("language", "ko"));

        Request request = new Request.Builder()
            .url(apiURL)
            .post(RequestBody.create(mediaType, requestBody.toString()))
            .addHeader("X-NCP-APIGW-API-KEY-ID", clientId)
            .addHeader("X-NCP-APIGW-API-KEY", clientSecret)
            .addHeader("Content-Type", "application/json")
            .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) return Optional.empty();

            JSONObject jsonResponse = new JSONObject(response.body().string());
            return Optional.ofNullable(jsonResponse.getJSONObject("summary").getString("content"));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
