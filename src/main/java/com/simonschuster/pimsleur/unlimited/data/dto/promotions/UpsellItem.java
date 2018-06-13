package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class UpsellItem {
    private String isbn;
    private String name;

    public UpsellItem(String isbn, String name) {
        this.isbn = isbn;
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return "Sean will provide this, we don't have it yet.";
    }
}
