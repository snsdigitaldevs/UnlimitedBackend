package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Image;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.MediaItem;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse.PcmFreeCourseResponse;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse.PcmFreeCourseResultData;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
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
import static java.util.stream.IntStream.rangeClosed;

@Service
public class PcmFreeCourseService {

    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private AppIdService appIdService;

    public List<Course> getPcmFreeCourseInfos(String productCode, String storeDomain) {

        PcmFreeCourseResponse pcmFreeCourseResponse = postToEdt(createPostBody(productCode, storeDomain),
                configuration.getProperty("edt.api.pcmFreeCourseApiUrl"),
                PcmFreeCourseResponse.class);

        PcmFreeCourseResultData resultData = pcmFreeCourseResponse.getResultData();
        if (resultData != null) {
            List<Lesson> lessons = createLessons(resultData);
            setAudioLinkForFirstLesson(resultData, lessons);

            return singletonList(createCourse(productCode, resultData, lessons));
        } else {
            return emptyList();
        }
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

    private void setAudioLinkForFirstLesson(PcmFreeCourseResultData resultData, List<Lesson> lessons) {
        Optional<Lesson> first = lessons.stream()
                .filter(x -> Objects.equals(x.getLessonNumber(), "01"))
                .findFirst();
        first.ifPresent(lesson -> lesson.setAudioLink(resultData.getProductsAudioUrl()));
    }

    private List<Lesson> createLessons(PcmFreeCourseResultData resultData) {
        List<Lesson> lessonsFromMediaSet = createLessonsFromMediaSet(resultData);

        boolean isMediaSetInsufficient = resultData.getAdditionalProductData() != null && lessonsFromMediaSet.size() == 1;
        if (isMediaSetInsufficient) {
            return createLessonsFromAdditionalInfo(resultData, lessonsFromMediaSet);
        }

        return lessonsFromMediaSet;
    }

    private List<Lesson> createLessonsFromAdditionalInfo(PcmFreeCourseResultData resultData, List<Lesson> lessonsFromMediaSet) {
        Lesson lesson1 = lessonsFromMediaSet.get(0);
        int numberOfLessons = resultData.getAdditionalProductData().getLevel1FullCourseTotalLessons();

        for(int i = 2; i <= numberOfLessons; i++){
            String lessonNumberString = String.format("%02d", i);
            Lesson lesson = new Lesson();
            lesson.setName(lesson1.getName().split(" ")[0] + " " + lessonNumberString);
            lesson.setLessonNumber(lessonNumberString);
            lesson.setImage(lesson1.getImage());
            lessonsFromMediaSet.add(lesson);
        }
        return lessonsFromMediaSet;
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
