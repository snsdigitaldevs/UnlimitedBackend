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
            new FreeLessonDto("French", "9781508243281", true),
            new FreeLessonDto("German", "9781508243298", true),
            new FreeLessonDto("Italian", "9781508243304", true),
            new FreeLessonDto("Spanish", "9781508243274", true),
            new FreeLessonDto("Russian", "9781508243335", true),
            new FreeLessonDto("Brazilian Portuguese", "9781508243342", true),
            new FreeLessonDto("Mandarin Chinese", "9781508243328", true),
            new FreeLessonDto("ESL Spanish", "9781508243359", true),
            new FreeLessonDto("Japanese", "9781508243311", true)
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
