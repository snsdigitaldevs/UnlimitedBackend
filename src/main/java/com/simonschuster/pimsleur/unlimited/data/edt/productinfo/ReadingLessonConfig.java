package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReadingLessonConfig {
    @JsonProperty("displayXlitHelpFromMetadata")
    private Boolean displayXlitHelpFromMetadata;
    @JsonProperty("showAlphabetChartLabel")
    private String showAlphabetChartLabel;
    @JsonProperty("showTranslitLabel")
    private String showTranslitLabel;
    @JsonProperty("showTranslitOptionEnabled")
    private Boolean showTranslitOptionEnabled;
    @JsonProperty("showTranslitByDefault")
    private Boolean showTranslitByDefault;
    @JsonProperty("alphabetChartSize")
    private String alphabetChartSize;
    @JsonProperty("alphabetChartPosition")
    private String alphabetChartPosition;
    @JsonProperty("disableAlphabetChart")
    private String disableAlphabetChart;

    public Boolean getDisplayXlitHelpFromMetadata() {
        return displayXlitHelpFromMetadata;
    }

    public void setDisplayXlitHelpFromMetadata(Boolean displayXlitHelpFromMetadata) {
        this.displayXlitHelpFromMetadata = displayXlitHelpFromMetadata;
    }

    public String getShowAlphabetChartLabel() {
        return showAlphabetChartLabel;
    }

    public void setShowAlphabetChartLabel(String showAlphabetChartLabel) {
        this.showAlphabetChartLabel = showAlphabetChartLabel;
    }

    public String getShowTranslitLabel() {
        return showTranslitLabel;
    }

    public void setShowTranslitLabel(String showTranslitLabel) {
        this.showTranslitLabel = showTranslitLabel;
    }

    public Boolean getShowTranslitOptionEnabled() {
        return showTranslitOptionEnabled;
    }

    public void setShowTranslitOptionEnabled(Boolean showTranslitOptionEnabled) {
        this.showTranslitOptionEnabled = showTranslitOptionEnabled;
    }

    public Boolean getShowTranslitByDefault() {
        return showTranslitByDefault;
    }

    public void setShowTranslitByDefault(Boolean showTranslitByDefault) {
        this.showTranslitByDefault = showTranslitByDefault;
    }

    public String getAlphabetChartSize() {
        return alphabetChartSize;
    }

    public void setAlphabetChartSize(String alphabetChartSize) {
        this.alphabetChartSize = alphabetChartSize;
    }

    public String getAlphabetChartPosition() {
        return alphabetChartPosition;
    }

    public void setAlphabetChartPosition(String alphabetChartPosition) {
        this.alphabetChartPosition = alphabetChartPosition;
    }

    public String getDisableAlphabetChart() {
        return disableAlphabetChart;
    }

    public void setDisableAlphabetChart(String disableAlphabetChart) {
        this.disableAlphabetChart = disableAlphabetChart;
    }
}
