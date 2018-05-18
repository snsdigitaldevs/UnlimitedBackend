package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active;

public class ActivateResultDTO {
    private String isbn;
    private Boolean isActivated;
    private Integer activatedTime;

    public ActivateResultDTO(String isbn, Boolean isActivated) {
        this.isbn = isbn;
        this.isActivated = isActivated;
    }

    public String getIsbn() {
        return isbn;
    }

    public Boolean getActivated() {

        return isActivated;
    }

    public Integer getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Integer activatedTime) {
        this.activatedTime = activatedTime;
    }
}
