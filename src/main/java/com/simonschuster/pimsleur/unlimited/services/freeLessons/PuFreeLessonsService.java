package com.simonschuster.pimsleur.unlimited.services.freeLessons;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.PU_FREE_LESSONS;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@Service
public class PuFreeLessonsService {

    private static List<String> pcmFreeLessonsToIgnore = asList(
            "9781442307674",//French
            "9781442308923",//German
            "9781442314931",//Italian
            "9781442313583",//Spanish
            "9781442312463",//Russian
            "9781442374140",//Brazilian Portuguese
            "9781442316034",//Mandarin Chinese
            "9781442328686",//ESL Spanish
            "9781442310223"//Japanese
    );

    public List<AvailableProductDto> mergePuWithPcmFreeLessons(List<AvailableProductDto> pcmFreeLessons) {
        Stream<AvailableProductDto> pcmFreeLessonsWithout9PuCourses =
                pcmFreeLessons.stream()
                        .filter(pcmFreeLesson -> pcmFreeLessonsToIgnore.stream()
                                .noneMatch(ignored -> ignored.equals(pcmFreeLesson.getProductCode())));

        return concat(pcmFreeLessonsWithout9PuCourses, PU_FREE_LESSONS.stream())
                .sorted(comparing(AvailableProductDto::getLanguageName))
                .collect(toList());
    }
}
