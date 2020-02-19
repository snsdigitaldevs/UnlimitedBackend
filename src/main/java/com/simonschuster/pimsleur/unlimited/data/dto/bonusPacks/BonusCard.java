package com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.BasicCard;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BonusCard extends BasicCard {

    public BonusCard(String transliteration, String translation, String language, String mp3FileName) {
        super(transliteration, translation, language, mp3FileName);
    }
}
