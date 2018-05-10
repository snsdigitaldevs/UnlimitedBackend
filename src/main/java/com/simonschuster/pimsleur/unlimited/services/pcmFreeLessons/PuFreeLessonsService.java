package com.simonschuster.pimsleur.unlimited.services.pcmFreeLessons;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.FreeLessonDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@Service
public class PuFreeLessonsService {

    private static List<FreeLessonDto> puFreeLessons = asList(
            new FreeLessonDto("9781508243281", "French", true),
            new FreeLessonDto("9781508243298", "German", true),
            new FreeLessonDto("9781508243304", "Italian", true),
            new FreeLessonDto("9781508243274", "Spanish", true),
            new FreeLessonDto("9781508243335", "Russian", true),
            new FreeLessonDto("9781508243342", "Brazilian Portuguese", true),
            new FreeLessonDto("9781508243328", "Mandarin Chinese", true),
            new FreeLessonDto("9781508243359", "ESL Spanish", true),
            new FreeLessonDto("9781508243311", "Japanese", true)
    );

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

    public List<FreeLessonDto> mergePuWithPcmFreeLessons(List<FreeLessonDto> pcmFreeLessons) {
        Stream<FreeLessonDto> pcmFreeLessonsWithout9PuCourses =
                pcmFreeLessons.stream()
                        .filter(pcmFreeLesson -> pcmFreeLessonsToIgnore.stream()
                                .noneMatch(ignored -> ignored.equals(pcmFreeLesson.getProductCode())));

        return concat(pcmFreeLessonsWithout9PuCourses, puFreeLessons.stream())
                .collect(toList());
    }
}
