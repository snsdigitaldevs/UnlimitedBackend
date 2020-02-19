package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class FlashCard extends BasicCard{
    public FlashCard(String transliteration, String translation, String language, String mp3FileName) {
        super(transliteration, translation, language, mp3FileName);
    }
}
