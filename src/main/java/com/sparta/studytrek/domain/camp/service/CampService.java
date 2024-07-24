package com.sparta.studytrek.domain.camp.service;

import com.sparta.studytrek.domain.camp.entity.Camp;
import com.sparta.studytrek.domain.camp.repository.CampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampService {

    private final CampRepository campRepository;

    public Camp findByName(String campName) {
        return campRepository.findByName(campName)
                .orElseThrow(() -> new IllegalArgumentException("Camp not found"));
    }

    public Camp findById(Long id) {
        return campRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Camp not found"));
    }
}
