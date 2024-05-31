package com.tatoeba.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "eng_vie_translation")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class EngVieTranslationEntity {
    @Id
    private Long id;
    @Column(name = "eng_id")
    private Integer engId;
    @Column(name = "eng_text", columnDefinition = "text")
    private String engText;
    @Column(name = "eng_audio_url")
    private String engAudioUrl;
    @Column(name = "vie_id")
    private Integer vieId;
    @Column(name = "vie_text", columnDefinition = "text")
    private String vieText;
}
