package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse.PcmFreeCourseResponse;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse.PcmFreeCourseResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.isOneOfNineBig;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Service
public class PcmFreeCourseService {

    @Autowired
    private ApplicationConfiguration configuration;

    public List<Course> getPcmFreeCourseInfos(String productCode) {

        PcmFreeCourseResponse pcmFreeCourseResponse = postToEdt(
                createPostBody(productCode),
                configuration.getProperty("edt.api.pcmFreeCourseApiUrl"),
                PcmFreeCourseResponse.class);

        PcmFreeCourseResultData resultData = pcmFreeCourseResponse.getResultData();
        if (resultData != null) {
            List<Lesson> lessons = createLessons(resultData);
            setAudioLinkForFirstLessons(resultData, lessons);

            Course course = createCourse(productCode, resultData, lessons);
            return singletonList(course);
        } else {
            return emptyList();
        }
    }

    private HttpEntity<String> createPostBody(String productCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(
                String.format(configuration.getApiParameter("pcmFreeCourseparameters"), productCode), headers);
    }

    private Course createCourse(String productCode, PcmFreeCourseResultData resultData, List<Lesson> lessons) {
        Course course = new Course();
        course.setLessons(lessons);

        course.setCourseName(resultData.getProductsName());
        course.setLanguageName(resultData.getProductsLanguageName());
        course.setLevel(new Long(resultData.getProductsLevel()).intValue());
        course.setProductCode(productCode);
        course.setIsFree(true);
        course.setIsOneOfNineBig(isOneOfNineBig(course.getLanguageName()));
        return course;
    }

    private void setAudioLinkForFirstLessons(PcmFreeCourseResultData resultData, List<Lesson> lessons) {
        Optional<Lesson> first = lessons.stream()
                .filter(x -> Objects.equals(x.getLessonNumber(), "01"))
                .findFirst();
        first.ifPresent(lesson -> lesson.setAudioLink(resultData.getProductsAudioUrl()));
    }

    private List<Lesson> createLessons(PcmFreeCourseResultData resultData) {
        return resultData.getMediaSet().getChildMediaSets().stream()
                .flatMap(mediaSet -> mediaSet.getMediaItems().stream())
                .filter(mediaItem -> mediaItem.getMediaItemFilename().contains(".mp3") && mediaItem.getMediaItemFilename().contains("Unit"))
                .map(mediaItem -> {
                    Lesson lesson = new Lesson();
                    lesson.setName(mediaItem.getMediaItemTitle());
                    lesson.setLessonNumber(mediaItem.getMediaItemTitle().split(" ")[1]);
                    return lesson;
                }).collect(toList());
    }
}
