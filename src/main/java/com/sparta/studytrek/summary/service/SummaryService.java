package com.sparta.studytrek.summary.service;

import com.sparta.studytrek.domain.camp.entity.Camp;
import com.sparta.studytrek.domain.camp.repository.CampRepository;
import java.util.List;
import java.util.Optional;
import javax.swing.Spring;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final CampRepository campRepository;

    private static final Logger logger = LoggerFactory.getLogger(SummaryService.class);
    public static final int DEFAULT_SUMMARY_COUNT = 2;
    public static final String DEFAULT_LANGAUGE = "ko";

    @Value("${NAVER_CLOVA_CLIENT_ID}")
    private String clientId;

    @Value("${NAVER_CLOVA_CLIENT_SECRET}")
    private String clientSecret;

    private final String apiURL = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public Optional<String> summarizeText(List<String> texts, Long campId) {
        return summarizeText(texts, DEFAULT_LANGAUGE, DEFAULT_SUMMARY_COUNT, campId);
    }

    public Optional<String> summarizeText(List<String> texts, String language, int summaryCount, Long campId) {
        JSONObject requestBody = new JSONObject()
            .put("document", new JSONObject().put("content", String.join("\n", texts)))
            .put("option", new JSONObject()
                .put("language", language)
                .put("summaryCount", summaryCount));

        Request request = new Request.Builder()
            .url(apiURL)
            .post(RequestBody.create(JSON, requestBody.toString()))
            .addHeader("X-NCP-APIGW-API-KEY-ID", clientId)
            .addHeader("X-NCP-APIGW-API-KEY", clientSecret)
            .addHeader("Content-Type", "application/json")
            .build();

        return getSummarizeText(texts, request)
            .map(summary -> updateCampSummary(campId, summary));
    }

    private @NotNull Optional<String> getSummarizeText(List<String> texts, Request request) {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("API request failed: {}", response);
                return Optional.empty();
            }
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            String summary = jsonResponse.getString("summary");

            logger.info("Summary generated successfully for texts: {}", texts);
            return Optional.of(summary);
        } catch (Exception e) {
            logger.error("Error while summarizing text", e);
            return Optional.empty();
        }
    }

    private String updateCampSummary(Long campId, String summary) {
        Optional<Camp> camp = campRepository.findById(campId);
        if (camp.isPresent()) {
            Camp campEntity = camp.get();
            campEntity.updateSummary(summary);
            campRepository.save(campEntity);
            return summary;
        } else {
            logger.error("Camp with id {} not found", campId);
            return "Camp Not Found";
        }
    }
}
