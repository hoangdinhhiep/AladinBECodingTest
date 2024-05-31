package com.tatoeba.controllers;

import com.tatoeba.commons.Constant;
import com.tatoeba.mappers.EngVieTranslationMapper;
import com.tatoeba.models.dto.BaseEntityPageProduce;
import com.tatoeba.models.dto.BaseResponseDto;
import com.tatoeba.models.dto.produces.EngVieTranslationProduce;
import com.tatoeba.services.EngVieTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${public_api_prefix_path}/translations", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class EngVieTranslationController extends BaseController {
    private final EngVieTranslationService mTranslationService;
    private final EngVieTranslationMapper mTranslationMapper;

    @GetMapping
    public ResponseEntity<BaseResponseDto> getTranslations(
            @RequestParam(defaultValue = Constant.Pageable.DEFAULT_PAGEABLE_PAGE) Integer page,
            @RequestParam(defaultValue = Constant.Pageable.DEFAULT_PAGEABLE_SIZE) Integer size) {
        Page<EngVieTranslationProduce> producePage = mTranslationService.findAll(page, size)
                .map(mTranslationMapper::toProduce);

        return ok(BaseEntityPageProduce.build(producePage));
    }
}
