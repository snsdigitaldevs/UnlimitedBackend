package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

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
}
