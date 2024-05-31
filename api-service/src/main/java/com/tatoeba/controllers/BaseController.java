package com.tatoeba.controllers;

import com.tatoeba.models.dto.BaseResponseDto;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected ResponseEntity<BaseResponseDto> ok(Object data) {
        return ResponseEntity.ok(BaseResponseDto.builder()
                .isSuccess(true)
                .data(data)
                .build());
    }
}
