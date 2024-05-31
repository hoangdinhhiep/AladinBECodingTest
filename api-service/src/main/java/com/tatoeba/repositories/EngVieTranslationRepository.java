package com.tatoeba.repositories;

import com.tatoeba.entities.EngVieTranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngVieTranslationRepository extends JpaRepository<EngVieTranslationEntity, Long> {
}
