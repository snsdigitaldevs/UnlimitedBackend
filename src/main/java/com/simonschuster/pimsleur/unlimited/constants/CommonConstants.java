package com.simonschuster.pimsleur.unlimited.constants;

import com.google.common.collect.Sets;

import java.util.Set;

public class CommonConstants {
    
    public static final String UK = "UK";
    public static final String CANADA = "CA";
    public static final String USA = "US";
    public static final String AUSTRALIA = "AU";
    public static final String UNDEFINED = "undefined";
    
    private CommonConstants() {
    }
    
    public static final Set<String> ARABIC_PU_ISBN = Sets
        .newHashSet("9781797115139", "9781797115108", "9781797115115", "9781797115122",
            "9781797112312", "9781797112237", "9781797112244", "9781797112251");
    
    public static final Set<String> HEBREW_PU_ISBN = Sets
        .newHashSet("9781797114446", "9781797114514", "9781797114552", "9781797115214");
    
}
