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
    private static final int LESSON_NUMBER_OF_LEVEL = 30;
    private static final String PREFIX_FOR_IMAGE_OF_PU = "https://install.pimsleurunlimited.com/staging_n/desktop/";
    private static final String PREFIX_FOR_AUDIO_OF_PU = "https://install.pimsleurunlimited.com/staging_n/common/";
    private static final Logger logger = LoggerFactory.getLogger(AggregatedProductInfo.class);

    private ProductInfoFromUnlimited productInfoFromPU;
    private ProductInfoFromPCM productInfoFromPCM;
    private Map<String, Map<String, Integer>> mediaSetInfo;
    private LessonsAudioInfo lessonAudioInfoFromPCM;

    public void setProductInfoFromPCM(ProductInfoFromPCM productInfoFromPCM) {
        this.productInfoFromPCM = productInfoFromPCM;
    }

    public ProductInfoFromPCM getProductInfoFromPCM() {
        return productInfoFromPCM;
    }

    public void setProductInfoFromPU(ProductInfoFromUnlimited productInfoFromPU) {
        this.productInfoFromPU = productInfoFromPU;
    }

    public ProductInfoFromUnlimited getProductInfoFromPU() {
        return productInfoFromPU;
    }

    public List<Course> toDto() {
        ArrayList<Course> courses = new ArrayList<>();

        if (productInfoFromPU != null) {
            setCourseInfoFromPU(courses, productInfoFromPU);
        } else if (productInfoFromPCM != null) {
            setCourseInfoFromPCM(courses, productInfoFromPCM, mediaSetInfo);
        }

        return courses;
    }

    private List<Course> setCourseInfoFromPU(List<Course> courses, ProductInfoFromUnlimited productInfoFromPU) {
        //Assumption: product code from frontend doesn't have kitted product code according to spike.
        //Spike scenario is: one user bought Mandarin level 1-5, the current product code got from customer info
        //API is a product code for one level instead of product code for level 1-5.

        Course course = new Course();
        Map<String, MediaSet> mediaSets = this.productInfoFromPU.getResultData().getMediaSets();
        mediaSets.forEach((currentProductCode, mediaSet) -> {
            course.setLanguageName(mediaSet.getCourseLanguageName());
            course.setLevel(mediaSet.getCourseLevel());
            try {
                transformLessonInfoFromPU(course, mediaSet);
            } catch (Exception e) {
                logger.error("Error occured when convert product info from PU EDT API.");
                e.printStackTrace();
                throw new UncheckedExecutionException(e);
            }
        });

        courses.add(course);

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
            getImageAndAudioFromPU(lesson, lessonItem, mediaSet);

            lessons.add(lesson);
        }

        course.setLessons(lessons);
    }

    private void getImageAndAudioFromPU(Lesson lesson, MediaItem lessonItem, MediaSet mediaSet) throws Exception {
        Image image = new Image();
        String audioUrl = "";

        String courseConfigKey = mediaSet.getCourseLanguageName().replace(" ", "_");
        CourseConfig courseConfig = productInfoFromPU.getResultData().getCourseConfigs().get(courseConfigKey);
        List<CourseLevelDef> levelDefs = courseConfig.getCourseLevelDefs();

        for (int i = 0; i < levelDefs.size(); i++) {
            CourseLevelDef courseLevelDef = levelDefs.get(i);
            if (courseLevelDef.getIsbn13().equals(mediaSet.getIsbn13())) {
                String pathMiddlePart = mediaSet.getCourseLanguageName().replace(" ", "").toLowerCase();

                String imageUrl = PREFIX_FOR_IMAGE_OF_PU + pathMiddlePart + "/"
                        + courseLevelDef.getMainLessonsFullImagePath() + lessonItem.getImageURL();
                audioUrl = PREFIX_FOR_AUDIO_OF_PU + pathMiddlePart + "/"
                        + courseLevelDef.getAudioPath() + lessonItem.getFilename();

                image.setFullImageAddress(imageUrl);
                break;
            }
        }

        lesson.setImage(image);
        lesson.setAudioLink(audioUrl);
    }

    private List<Course> setCourseInfoFromPCM(List<Course> courses, ProductInfoFromPCM productInfoFromPCM, Map<String, Map<String, Integer>> mediaSetInfo) {
        Course course = new Course();
        course.setLanguageName(productInfoFromPCM.getOrderProduct().getProduct().getProductsLanguageName());
        //todo: set level and lesson info from mediasetinfo and productInfoFromPCM

        courses.add(course);
        return courses;
    }

    public void setMediaSetInfo(Map<String, Map<String, Integer>> mediaSetInfo) {
        this.mediaSetInfo = mediaSetInfo;
    }

    public Map<String, Map<String, Integer>> getMediaSetInfo() {
        return mediaSetInfo;
    }

    public void setLessonAudioInfoFromPCM(LessonsAudioInfo lessonAudioInfoFromPCM) {
        this.lessonAudioInfoFromPCM = lessonAudioInfoFromPCM;
    }

    public LessonsAudioInfo getLessonAudioInfoFromPCM() {
        return lessonAudioInfoFromPCM;
    }
}
