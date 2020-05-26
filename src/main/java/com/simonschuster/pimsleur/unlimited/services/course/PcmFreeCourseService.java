package com.simonschuster.pimsleur.unlimited.services.course;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.isOneOfNineBig;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import com.simonschuster.pimsleur.unlimited.aop.annotation.LogCostTime;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Image;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.MediaItem;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse.PcmFreeCourseResponse;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse.PcmFreeCourseResultData;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class PcmFreeCourseService {

    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private AppIdService appIdService;

    @LogCostTime
    public List<Course> getPcmFreeCourseInfos(String productCode, String storeDomain) {
        PcmFreeCourseResponse pcmFreeCourseResponse = postToEdt(createPostBody(productCode, storeDomain),
            configuration.getProperty("edt.api.pcmFreeCourseApiUrl"),
            PcmFreeCourseResponse.class);

        return pcmFreeCourseResponse.getResultData().map(data -> {
            List<Lesson> lessons = createLessons(data);
            return singletonList(createCourse(productCode, data, lessons));
        }).orElse(emptyList());
    }

    private Course createCourse(String productCode, PcmFreeCourseResultData resultData, List<Lesson> lessons) {
        Course course = new Course();
        course.setLessons(lessons);
        course.setCourseName(resultData.getProductsName());
        course.setLanguageName(resultData.getProductsLanguageName());
        course.setLevel((int) resultData.getProductsLevel());
        course.setProductCode(productCode);
        course.setIsFree(true);
        course.setIsOneOfNineBig(isOneOfNineBig(course.getLanguageName()));
        return course;
    }

    private List<Lesson> createLessons(PcmFreeCourseResultData resultData) {
        List<Lesson> lessonsFromMediaSet = createLessonsFromMediaSet(resultData);

        boolean isMediaSetInsufficient = resultData.getAdditionalProductData() != null && lessonsFromMediaSet.size() == 1;
        if (isMediaSetInsufficient) {
            Lesson  firstLesson = lessonsFromMediaSet.get(0);
            List<Lesson> lessonsFromAdditionalInfo =  createLessonsFromAdditionalInfo(resultData, firstLesson);
            lessonsFromMediaSet.addAll(lessonsFromAdditionalInfo);
        }

        return lessonsFromMediaSet;
    }

    private List<Lesson> createLessonsFromAdditionalInfo(PcmFreeCourseResultData resultData, Lesson firstLesson) {
        List<Lesson> lessons = new ArrayList<>();
        int numberOfLessons = resultData.getAdditionalProductData().getLevel1FullCourseTotalLessons();

        for (int i = 2; i <= numberOfLessons; i++) {
            String lessonNumberString = String.format("%02d", i);
            Lesson lesson = new Lesson();
            lesson.setName(firstLesson.getName().split(" ")[0] + " " + lessonNumberString);
            lesson.setLessonNumber(lessonNumberString);
            lesson.setImage(firstLesson.getImage());
            lessons.add(lesson);
        }
        return lessons;
    }

    private List<Lesson> createLessonsFromMediaSet(PcmFreeCourseResultData resultData) {
        Image pcmImage = resultData.getPcmImage();
        return resultData.getMediaSet().getChildMediaSets().stream()
            .flatMap(mediaSet -> mediaSet.getMediaItems().stream())
            .filter(MediaItem::isLesson)
            .map(mediaItem -> {
                Lesson lesson = new Lesson();
                lesson.setName(mediaItem.getMediaItemTitle());
                lesson.setMediaItemId(mediaItem.getMediaItemId());
                lesson.setLessonNumber(mediaItem.getMediaItemTitle().split(" ")[1]);
                if (lesson.getLessonNumber().equals("01")) {
                    lesson.setAudioLink(resultData.getProductsAudioUrl());
                }
                lesson.setImage(pcmImage);
                return lesson;
            }).collect(toList());
    }

    private HttpEntity<String> createPostBody(String productCode, String storeDomain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String appId = appIdService.getAppId(storeDomain);
        return new HttpEntity<>(
            String.format(configuration.getApiParameter("pcmFreeCourseparameters"), productCode, appId), headers);
    }
}
