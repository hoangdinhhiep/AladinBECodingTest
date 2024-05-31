package com.tatoeba.services.impl;

import com.tatoeba.entities.EngVieTranslationEntity;
import com.tatoeba.repositories.EngVieTranslationRepository;
import com.tatoeba.services.EngVieTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EngVieTranslationServiceImpl implements EngVieTranslationService {
    private final EngVieTranslationRepository mTranslationRepository;

    @Override
    public Page<EngVieTranslationEntity> findAll(int page, int size) {
        return mTranslationRepository.findAll(PageRequest.of(page, size));
    }
}
