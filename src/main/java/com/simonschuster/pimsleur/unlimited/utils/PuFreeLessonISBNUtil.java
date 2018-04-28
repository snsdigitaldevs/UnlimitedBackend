package com.simonschuster.pimsleur.unlimited.utils;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class PuFreeLessonISBNUtil {

    // key is PU free lesson isbn
    // value is PU normal isbn
    private static Map<String, String> freeISBNToNormalISBN =
            ImmutableMap.<String, String>builder()
                    .put("9781508243359", "9781508235927")
                    .put("9781508243311", "9781508222033")
                    .put("9781508243281", "9781508231165")
                    .put("9781508243298", "9781508231387")
                    .put("9781508243274", "9781508231509")
                    .put("9781508243335", "9781508231486")
                    .put("9781508243328", "9781508231431")
                    .put("9781508243304", "9781508231400")
                    .put("9781508243342", "9781508231448")
                    .build();

    public static String toNormalISBN(String productCode) {
        if (freeISBNToNormalISBN.containsKey(productCode)) {
            return freeISBNToNormalISBN.get(productCode);
        }
        return productCode;
    }
}
