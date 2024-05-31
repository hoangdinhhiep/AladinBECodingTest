package com.tatoeba.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
public class BaseResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonProperty("error_code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;
    @JsonProperty("is_success")
    private boolean isSuccess;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
}
