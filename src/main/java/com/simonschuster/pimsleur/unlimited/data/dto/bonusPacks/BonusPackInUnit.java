package com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks;


import java.util.ArrayList;
import java.util.List;

public class BonusPackInUnit {

    private Integer packGroupNumber;

    private List<BonusCard> bonusPack = new ArrayList<>();

    public Integer getPackGroupNumber() {
        return packGroupNumber;
    }

    public void setPackGroupNumber(Integer packGroupNumber) {
        this.packGroupNumber = packGroupNumber;
    }

    public List<BonusCard> getBonusPack() {
        return bonusPack;
    }

    public void setBonusPack(List<BonusCard> bonusPack) {
        this.bonusPack = bonusPack;
    }
}
