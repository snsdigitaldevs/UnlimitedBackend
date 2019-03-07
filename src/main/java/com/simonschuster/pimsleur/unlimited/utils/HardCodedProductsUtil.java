package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class HardCodedProductsUtil {
    public static List<AvailableProductDto> PU_FREE_LESSONS = asList(
            new AvailableProductDto("French","French", "9781508279709", true),
            new AvailableProductDto("German","German", "9781508243298", true),
            new AvailableProductDto("Italian","Italian", "9781508243304", true),
            new AvailableProductDto("Spanish","Spanish", "9781508283737", true),//!!
            new AvailableProductDto("Russian","Russian", "9781508243335", true),//!!skills
            new AvailableProductDto("Portuguese Brazilian","Portuguese Brazilian", "9781508243342", true),
            new AvailableProductDto("Chinese Mandarin","Chinese Mandarin", "9781508243328", true),//!!skills
            new AvailableProductDto("English for Spanish Speakers, Premium","ESL Spanish", "9781508243359", true),
            new AvailableProductDto("Japanese","Japanese", "9781508243311", true)
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
