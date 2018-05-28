package com.simonschuster.pimsleur.unlimited.utils;

import com.google.common.collect.ImmutableMap;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class HardCodedProductsUtil {
    public static List<AvailableProductDto> PU_FREE_LESSONS = asList(
            new AvailableProductDto("French", "9781508243281", true),
            new AvailableProductDto("German", "9781508243298", true),
            new AvailableProductDto("Italian", "9781508243304", true),
            new AvailableProductDto("Spanish", "9781508243274", true),
            new AvailableProductDto("Russian", "9781508243335", true),
            new AvailableProductDto("Portuguese Brazilian", "9781508243342", true),
            new AvailableProductDto("Chinese Mandarin", "9781508243328", true),
            new AvailableProductDto("ESL Spanish", "9781508243359", true),
            new AvailableProductDto("Japanese", "9781508243311", true)
    );

    private static final List<String> nineBigLanguageNames = PU_FREE_LESSONS.stream()
            .map(AvailableProductDto::getLanguageName)
            .collect(toList());

    private static final List<String> puFreeIsbns = PU_FREE_LESSONS.stream()
            .map(AvailableProductDto::getProductCode)
            .collect(toList());

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


    public static boolean isOneOfNineBig(String langName) {
        return nineBigLanguageNames.contains(langName);
    }

    public static String puFreeToPuNormalIsbn(String productCode) {
        if (freeISBNToNormalISBN.containsKey(productCode)) {
            return freeISBNToNormalISBN.get(productCode);
        }
        return productCode;
    }

    public static boolean isPuFreeLesson(String productCode) {
        return puFreeIsbns.contains(productCode);
    }
}
