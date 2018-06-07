package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class UpsellDto {
    private UpsellItem nextLevel;
    private UpsellItem nextVersion;

    public UpsellDto() {
    }

    public UpsellDto(UpsellItem nextLevel, UpsellItem nextVersion) {
        this.nextLevel = nextLevel;
        this.nextVersion = nextVersion;
    }

    public UpsellItem getNextLevel() {
        return nextLevel;
    }

    public UpsellItem getNextVersion() {
        return nextVersion;
    }
}
