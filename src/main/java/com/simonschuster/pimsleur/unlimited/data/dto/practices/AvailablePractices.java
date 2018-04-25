package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import java.util.List;

public class AvailablePractices {
    private List<PracticesInUnit> practicesInUnits;

    public AvailablePractices(List<PracticesInUnit> practicesInUnits) {
        this.practicesInUnits = practicesInUnits;
    }

    public List<PracticesInUnit> getPracticesInUnits() {
        return practicesInUnits;
    }
}
