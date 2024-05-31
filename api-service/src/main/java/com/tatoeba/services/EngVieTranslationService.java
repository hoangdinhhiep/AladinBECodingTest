package com.tatoeba.services;

import com.tatoeba.entities.EngVieTranslationEntity;
import org.springframework.data.domain.Page;

public interface EngVieTranslationService {
    Page<EngVieTranslationEntity> findAll(int page, int size);
}
