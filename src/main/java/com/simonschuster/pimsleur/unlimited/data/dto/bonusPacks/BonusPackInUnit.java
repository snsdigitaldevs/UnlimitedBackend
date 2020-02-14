package com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks;


import java.util.ArrayList;
import java.util.List;

public class BonusPackInUnit {

    private Integer unitNumber;

    private List<BonusCard> bonusPack = new ArrayList<>();

    public Integer getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(Integer unitNumber) {
        this.unitNumber = unitNumber;
    }

    public List<BonusCard> getBonusPack() {
        return bonusPack;
    }

    public void setBonusPack(List<BonusCard> bonusPack) {
        this.bonusPack = bonusPack;
    }
}
