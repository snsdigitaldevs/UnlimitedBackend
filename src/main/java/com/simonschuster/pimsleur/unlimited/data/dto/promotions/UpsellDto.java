package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

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
