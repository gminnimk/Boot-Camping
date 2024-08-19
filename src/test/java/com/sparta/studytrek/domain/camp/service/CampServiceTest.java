package com.sparta.studytrek.domain.camp.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.config.aws.S3Uploader;
import com.sparta.studytrek.domain.camp.dto.CampResponseDto;
import com.sparta.studytrek.domain.camp.entity.Camp;
import com.sparta.studytrek.domain.camp.repository.CampRepository;
import com.sparta.studytrek.domain.rank.entity.Rank;
import com.sparta.studytrek.domain.rank.repository.RankRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CampServiceTest {

    @Mock
    private CampRepository campRepository;

    @Mock
    private RankRepository rankRepository;

    @Mock
    private S3Uploader s3Uploader;

    @InjectMocks
    private CampService campService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // (1). createCamp 테스트 코드
    @Test
    void testCreateCamp_Success() throws IOException {
        // Given
        String campName = "New Camp";
        String description = "Camp Description";
        String imageUrl = "http://image.url";
        MultipartFile imageFile = mock(MultipartFile.class);

        when(campRepository.findByName(campName)).thenReturn(Optional.empty());
        when(s3Uploader.upload(imageFile, "camp-images")).thenReturn(imageUrl);
        when(campRepository.save(any(Camp.class))).thenAnswer(invocation -> {
            Camp camp = invocation.getArgument(0);
            return camp; // ID는 실제 저장 과정에서 설정됨
        });
        when(rankRepository.findMaxRanking()).thenReturn(Optional.of(10));

        // When
        CampResponseDto response = campService.createCamp(campName, description, imageFile);

        // Then
        assertNotNull(response);
        assertEquals(campName, response.name());
        assertEquals(description, response.description());
        assertEquals(imageUrl, response.imageUrl());

        verify(campRepository, times(1)).findByName(campName);
        verify(s3Uploader, times(1)).upload(imageFile, "camp-images");
        verify(campRepository, times(1)).save(any(Camp.class));
        verify(rankRepository, times(1)).save(any(Rank.class));
    }

    // 캠프 이름이 중복되는 경우
    @Test
    void testCreateCamp_DuplicateCampName() throws IOException {
        // Given
        String campName = "Existing Camp";
        String description = "Camp Description";
        MultipartFile imageFile = mock(MultipartFile.class);

        when(campRepository.findByName(campName)).thenReturn(Optional.of(new Camp()));

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            campService.createCamp(campName, description, imageFile);
        });

        // Then
        assertEquals(ErrorCode.DUPLICATE_CAMP_NAME, exception.getErrorCode());

        verify(campRepository, times(1)).findByName(campName);
        verify(s3Uploader, times(0)).upload(any(), anyString());
        verify(campRepository, times(0)).save(any(Camp.class));
        verify(rankRepository, times(0)).save(any(Rank.class));
    }

    // 이미지 파일이 없는 경우
    @Test
    void testCreateCamp_NoImageFile() throws IOException {
        // Given
        String campName = "New Camp";
        String description = "Camp Description";
        MultipartFile imageFile = null;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            campService.createCamp(campName, description, imageFile);
        });

        // Then
        assertEquals("이미지 파일이 필요합니다.", exception.getMessage());

        verify(campRepository, times(1)).findByName(campName);
        verify(s3Uploader, times(0)).upload(any(), anyString());
        verify(campRepository, times(0)).save(any(Camp.class));
        verify(rankRepository, times(0)).save(any(Rank.class));
    }

    // (2) updateCampRankingsBasedOnLikes 테스트 코드
    @Test
    void testUpdateCampRankingsBasedOnLikes() {

        // Given
        Camp camp1 = new Camp("Camp 1", "Description 1", "imageUrl1");
        Camp camp2 = new Camp("Camp 2", "Description 2", "imageUrl2");
        Camp camp3 = new Camp("Camp 3", "Description 3", "imageUrl3");

        // 좋아요 수 설정
        camp1.incrementLikes();
        camp1.incrementLikes();
        camp2.incrementLikes();
        camp3.incrementLikes();
        camp3.incrementLikes();
        camp3.incrementLikes();

        // 기존 Rank 엔티티가 없다고 가정
        when(campRepository.findAll()).thenReturn(List.of(camp1, camp2, camp3));
        when(rankRepository.findByCamp(any(Camp.class))).thenReturn(Optional.empty());

        // When
        campService.updateCampRankingsBasedOnLikes();

        // Then
        verify(rankRepository, times(1)).save(argThat(rank ->
            rank.getCamp().equals(camp3) && rank.getRanking() == 1
        ));
        verify(rankRepository, times(1)).save(argThat(rank ->
            rank.getCamp().equals(camp1) && rank.getRanking() == 2
        ));
        verify(rankRepository, times(1)).save(argThat(rank ->
            rank.getCamp().equals(camp2) && rank.getRanking() == 3
        ));
    }
}
