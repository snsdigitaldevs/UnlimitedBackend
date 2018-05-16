package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.FreeLessonDto;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class HardCodedProductsUtil {
    public static List<FreeLessonDto> PU_FREE_LESSONS = asList(
            new FreeLessonDto("French", "9781508243281", true),
            new FreeLessonDto("German", "9781508243298", true),
            new FreeLessonDto("Italian", "9781508243304", true),
            new FreeLessonDto("Spanish", "9781508243274", true),
            new FreeLessonDto("Russian", "9781508243335", true),
            new FreeLessonDto("Portuguese Brazilian", "9781508243342", true),
            new FreeLessonDto("Chinese Mandarin", "9781508243328", true),
            new FreeLessonDto("ESL Spanish", "9781508243359", true),
            new FreeLessonDto("Japanese", "9781508243311", true)
    );

    private static final List<String> nineBigLanguageNames = PU_FREE_LESSONS.stream()
            .map(FreeLessonDto::getLanguageName)
            .collect(toList());

    public static boolean isOneOfNineBig(String langName) {
        return nineBigLanguageNames.contains(langName);
    }
}
