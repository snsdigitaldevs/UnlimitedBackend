package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.google.common.util.concurrent.UncheckedExecutionException;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Image;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregatedProductInfo {
    private static final String PREFIX_FOR_IMAGE_OF_PU = "https://install.pimsleurunlimited.com/staging_n/desktop/";
    private static final String PREFIX_FOR_AUDIO_OF_PU = "https://install.pimsleurunlimited.com/staging_n/common/";
    private static final Logger logger = LoggerFactory.getLogger(AggregatedProductInfo.class);

    private PuProductInfo puProductInfo;
    private PcmProduct pcmProduct;
    private Map<String, List<Lesson>> lessonAudioInfoFromPCM;

    public void setPcmProduct(PcmProduct pcmProduct) {
        this.pcmProduct = pcmProduct;
    }

    public PcmProduct getPcmProduct() {
        return pcmProduct;
    }

    public void setPuProductInfo(PuProductInfo productInfoFromPU) {
        this.puProductInfo = productInfoFromPU;
    }

    public PuProductInfo getPuProductInfo() {
        return puProductInfo;
    }

    public List<Course> toDto() {
        ArrayList<Course> courses = new ArrayList<>();

        if (puProductInfo != null) {
            buildCourseInfoFromPU(courses);
        } else if (pcmProduct != null) {
            buildCourseInfoFromPCM(courses, pcmProduct, lessonAudioInfoFromPCM);
        }

        return courses;
    }

    private List<Course> buildCourseInfoFromPU(List<Course> courses) {
        // If kitted product code is passed in, a list of courses will be returned.
        Map<String, MediaSet> mediaSets = this.puProductInfo.getResultData().getMediaSets();
        mediaSets.forEach((currentProductCode, mediaSet) -> {
            Course course = new Course();
            course.setLanguageName(mediaSet.getCourseLanguageName());
            course.setLevel(mediaSet.getCourseLevel());
            try {
                transformLessonInfoFromPU(course, mediaSet);
            } catch (Exception e) {
                logger.error("Error occured when convert product info from PU EDT API.");
                e.printStackTrace();
                throw new UncheckedExecutionException(e);
            }
            courses.add(course);
        });

        return courses;
    }

    private List<Course> buildCourseInfoFromPCM(List<Course> courses, PcmProduct productInfoFromPCM, Map<String, List<Lesson>> lessonAudioInfoFromPCM) {

        productInfoFromPCM.getOrdersProductList().forEach((orderProductCode, orderProductInfo) -> {
            lessonAudioInfoFromPCM.forEach((level, lessonInfoForOneLevel) -> {
                Course course = new Course();
                course.setLanguageName(orderProductInfo.getProduct().getProductsLanguageName());
                course.setLevel(Integer.parseInt(level));
                course.setLessons(lessonInfoForOneLevel);

                courses.add(course);
            });
        });

        return courses;
    }

    private void transformLessonInfoFromPU(Course course, MediaSet mediaSet) throws Exception {
        List<Lesson> lessons = new ArrayList<>();

        //Get media items which contains 'imageDescription', we think those items are the ones contains useful info.
        List<MediaItem> mediaItems = mediaSet.getMediaItems().stream()
                .filter((mediaItem) -> (!StringUtils.isEmpty(mediaItem.getImageDescription())))
                .collect(Collectors.toList());

        for (MediaItem lessonItem : mediaItems) {

            Lesson lesson = new Lesson();
            lesson.setLevel(course.getLevel());
            lesson.setLessonNumber(lessonItem.getUnit());
            lesson.setName(lessonItem.getTitle());
            lesson.setImageDescription(lessonItem.getImageDescription());
            lesson.setMediaItemId(lessonItem.getMediaItemId());
            getImageAndAudioFromPU(lesson, lessonItem, mediaSet);

            lessons.add(lesson);
        }

        course.setLessons(lessons);
    }

    private void getImageAndAudioFromPU(Lesson lesson, MediaItem lessonItem, MediaSet mediaSet) throws Exception {
        Image image = new Image();
        String audioUrl = "";

        String courseConfigKey = mediaSet.getCourseLanguageName().replace(" ", "_");
        CourseConfig courseConfig = puProductInfo.getResultData().getCourseConfigs().get(courseConfigKey);
        List<CourseLevelDef> levelDefs = courseConfig.getCourseLevelDefs();

        for (int i = 0; i < levelDefs.size(); i++) {
            CourseLevelDef courseLevelDef = levelDefs.get(i);
            if (courseLevelDef.getIsbn13().equals(mediaSet.getIsbn13())) {
                String pathMiddlePart = mediaSet.getCourseLanguageName().replace(" ", "").toLowerCase();

                String fullImageAddress = PREFIX_FOR_IMAGE_OF_PU + pathMiddlePart + "/"
                        + courseLevelDef.getMainLessonsFullImagePath() + lessonItem.getImageURL();
                String thumbImageAddress = PREFIX_FOR_IMAGE_OF_PU + pathMiddlePart + "/"
                        + courseLevelDef.getMainLessonsThumbImagePath() + lessonItem.getImageURL();
                audioUrl = PREFIX_FOR_AUDIO_OF_PU + pathMiddlePart + "/"
                        + courseLevelDef.getAudioPath() + lessonItem.getFilename();

                image.setFullImageAddress(fullImageAddress);
                image.setThumbImageAddress(thumbImageAddress);
                break;
            }
        }

        lesson.setImage(image);
        lesson.setAudioLink(audioUrl);
    }

    public void setPcmAudioInfo(Map<String, List<Lesson>> lessonAudioInfoFromPCM) {
        this.lessonAudioInfoFromPCM = lessonAudioInfoFromPCM;
    }
}
