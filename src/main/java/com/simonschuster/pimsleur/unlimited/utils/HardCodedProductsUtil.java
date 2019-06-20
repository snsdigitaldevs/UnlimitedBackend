package com.simonschuster.pimsleur.unlimited.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class HardCodedProductsUtil {
    public static List<AvailableProductDto> PU_FREE_LESSONS = readJsonFile();

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

    private static List<AvailableProductDto> readJsonFile() {
        InputStream fileStream = HardCodedProductsUtil.class.getClassLoader()
                .getResourceAsStream("isbn-mapping/puFreeLesson.json");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(fileStream, new TypeReference<List<AvailableProductDto>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
