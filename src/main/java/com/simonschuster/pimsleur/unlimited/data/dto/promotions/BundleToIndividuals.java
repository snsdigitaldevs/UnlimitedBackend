package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

import java.util.List;

public class BundleToIndividuals {
    private String bundleIsbn;
    private List<String> childIsbns;

    public BundleToIndividuals(String bundleIsbn, List<String> childIsbns) {
        this.bundleIsbn = bundleIsbn;
        this.childIsbns = childIsbns;
    }

    public String getBundleIsbn() {
        return bundleIsbn;
    }

    public List<String> getChildIsbns() {
        return childIsbns;
    }
}
