package com.tatoeba.models.dto.produces;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EngVieTranslationProduce {
    @JsonProperty("eng_id")
    private Integer engId;
    @JsonProperty("eng_text")
    private String engText;
    @JsonProperty("eng_audio_url")
    private String engAudioUrl;
    @JsonProperty("vie_id")
    private Integer vieId;
    @JsonProperty("vie_text")
    private String vieText;
}
