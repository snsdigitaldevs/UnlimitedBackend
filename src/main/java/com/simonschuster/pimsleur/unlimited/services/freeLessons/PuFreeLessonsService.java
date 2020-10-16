package com.simonschuster.pimsleur.unlimited.services.freeLessons;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.PU_FREE_LESSONS;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@Service
public class PuFreeLessonsService {

    public List<AvailableProductDto> mergePuWithPcmFreeLessons(List<AvailableProductDto> pcmFreeLessons) {
        Stream<AvailableProductDto> pcmFreeLessonsWithoutOverlappingPU =
                pcmFreeLessons.stream()
                        .filter(pcmFree ->
                                PU_FREE_LESSONS.stream().noneMatch(puFree -> puFree.isSameLang(pcmFree)));

        return concat(pcmFreeLessonsWithoutOverlappingPU, PU_FREE_LESSONS.stream())
                .collect(toList());
    }
}
