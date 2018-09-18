package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class HardCodedProductsUtil {
    public static List<AvailableProductDto> PU_FREE_LESSONS = asList(
            new AvailableProductDto("French", "9781508279709", true),
            new AvailableProductDto("German", "9781508243298", true),
            new AvailableProductDto("Italian", "9781508243304", true),
            new AvailableProductDto("Spanish", "9781508243274", true),//!!
            new AvailableProductDto("Russian", "9781508243335", true),//!!skills
            new AvailableProductDto("Portuguese Brazilian", "9781508243342", true),
            new AvailableProductDto("Chinese Mandarin", "9781508243328", true),//!!skills
            new AvailableProductDto("ESL Spanish", "9781508243359", true),
            new AvailableProductDto("Japanese", "9781508243311", true)
    );

    private static final List<String> nineBigLanguageNames = PU_FREE_LESSONS.stream()
            .map(AvailableProductDto::getLanguageName)
            .collect(toList());

    public static final List<String> puFreeIsbns = PU_FREE_LESSONS.stream()
            .map(AvailableProductDto::getProductCode)
            .collect(toList());

    public static boolean isOneOfNineBig(String langName) {
        return nineBigLanguageNames.contains(langName);
    }

    public static boolean isPuFreeLesson(String productCode) {
        return puFreeIsbns.contains(productCode);
    }
}
