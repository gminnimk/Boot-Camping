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

    private final String apiURL = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public Optional<String> summarizeText(String text) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("document", new JSONObject().put("content", text));
        requestBody.put("option", new JSONObject().put("language", "ko"));

        Request request = new Request.Builder()
            .url(apiURL)
            .post(RequestBody.create(JSON, requestBody.toString()))
            .addHeader("X-NCP-APIGW-API-KEY-ID", clientId)
            .addHeader("X-NCP-APIGW-API-KEY", clientSecret)
            .addHeader("Content-Type", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return Optional.empty();

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            return Optional.ofNullable(jsonResponse.getJSONObject("summary").getString("content"));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public String getSummary(String content) {
        if (content == null || content.isEmpty()) {
            return "내용이 없습니다.";
        }

        return summarizeText(content).orElse("요약을 생성할 수 없습니다.");
    }
}
