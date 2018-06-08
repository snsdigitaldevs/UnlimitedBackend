package com.simonschuster.pimsleur.unlimited.integration;

import com.simonschuster.pimsleur.unlimited.controllers.AvailableProductsController;
import com.simonschuster.pimsleur.unlimited.controllers.ProductController;
import com.simonschuster.pimsleur.unlimited.data.dto.availableProducts.AvailableProductsDto;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AllESLProductInfoTest {
    @Autowired
    AvailableProductsController availableProductsController;
    @Autowired
    ProductController productController;

    @Ignore("This integration test need too long.")
    @Test
    public void shouldGetESLProductLessonsCorrectly() {
        //user email: super@thoughtworks.com, exclude PCM courses for 9 big languages.
        String allLanguageCoursesUserSub = "auth0%7C5af2871c6628ad6608677303";
        //user email: 9-big-lauguages-pcm-courses@thoughtworks.com, all PCM courses for 9 big languages.
        String allPCMCoursesForNineBigLanguagesSub = "auth0%7C5b18e3133e7e606465d37a0c";

        List<AvailableProductDto> allSupportedESLProducts = getAllSupportedESLProducts(allLanguageCoursesUserSub, allPCMCoursesForNineBigLanguagesSub);

        List<String> allESLLanguageNames = allSupportedESLProducts.stream()
                .filter(distinctByKey(AvailableProductDto::getLanguageName))
                .map(AvailableProductDto::getLanguageName)
                .collect(Collectors.toList());
        assertEquals(14, allESLLanguageNames.size());

        List<List<Course>> allESLProductsInfo = allSupportedESLProducts.stream()
                .map(eslProduct -> productController.getProductInfo(eslProduct.isPuProduct(),
                        false, eslProduct.getProductCode(), allLanguageCoursesUserSub))
                .collect(Collectors.toList());
        assertEquals(20, allESLProductsInfo.size());

        allESLProductsInfo.forEach(eslProductInfo -> {
            assertTrue(eslProductInfo.size() > 0);
            eslProductInfo.forEach(course -> {
                assertTrue(course.getLessons().size() > 0);
                course.getLessons().forEach(lesson -> {
                    assertFalse(StringUtils.isEmpty(lesson.getAudioLink()));
                });
                assertNotNull(course.getLanguageName());
                System.out.println(course.getCourseName());
            });
        });
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    private List<AvailableProductDto> getAllSupportedESLProducts(String allLanguageCoursesUserSub, String allPCMCoursesForNineBigLanguagesSub) {
        List<AvailableProductDto> allSupportedCourses = getAllSupportedCourses(allLanguageCoursesUserSub, allPCMCoursesForNineBigLanguagesSub);

        return allSupportedCourses.stream()
                .filter(availableProduct -> availableProduct.getLanguageName().startsWith("ESL"))
                .collect(Collectors.toList());
    }

    private List<AvailableProductDto> getAllSupportedCourses(String allLanguageCoursesUserSub, String allPCMCoursesForNineBigLanguagesSub) {
        AvailableProductsDto allLanguageCourses = availableProductsController.getAvailableProducts(allLanguageCoursesUserSub);
        AvailableProductsDto allPCMCoursesForNineBigLanguages = availableProductsController.getAvailableProducts(allPCMCoursesForNineBigLanguagesSub);

        List<AvailableProductDto> allSupportedCourses = new ArrayList<>();
        allSupportedCourses.addAll(allLanguageCourses.getPurchasedProducts());
        allSupportedCourses.addAll(allPCMCoursesForNineBigLanguages.getPurchasedProducts());

        return allSupportedCourses;
    }
}
