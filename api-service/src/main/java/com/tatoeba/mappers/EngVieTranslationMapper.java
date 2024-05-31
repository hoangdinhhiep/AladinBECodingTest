package com.tatoeba.mappers;

import com.tatoeba.entities.EngVieTranslationEntity;
import com.tatoeba.models.dto.produces.EngVieTranslationProduce;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EngVieTranslationMapper {
    EngVieTranslationProduce toProduce(EngVieTranslationEntity entity);
}
