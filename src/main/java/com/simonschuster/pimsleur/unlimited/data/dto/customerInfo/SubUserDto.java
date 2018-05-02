package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

public class SubUserDto {
    private String name;
    private String id;
    private boolean isRootSubUser;

    public SubUserDto(String name, String id, boolean isRootSubUser) {
        this.name = name;
        this.id = id;
        this.isRootSubUser = isRootSubUser;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean getIsRootSubUser() {
        return isRootSubUser;
    }
}
