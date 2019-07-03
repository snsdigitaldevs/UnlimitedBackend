package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class UpsellItem {
    private String isbn;
    private String baseISBN;
    private String name;
    private String description;
    private String webLink;
    private String pid;

    public UpsellItem(String isbn, String name, String webLink) {
        this.isbn = isbn;
        this.name = name;
        this.webLink = webLink;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBaseISBN(String baseISBN) {
        this.baseISBN = baseISBN;
    }

    public String getBaseISBN() {
        return baseISBN;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
