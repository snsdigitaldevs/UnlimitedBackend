package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Image;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregatedProductInfo {
    private static final int LESSON_NUMBER_OF_LEVEL = 30;
    private static final String PREFIX_FOR_IMAGE_OF_PU = "https://install.pimsleurunlimited.com/staging_n/desktop/";

    private ProductInfoFromUnlimited productInfoFromPU;
    private ProductInfoFromPCM productInfoFromPCM;

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

    public Course toDto() {
        Course course = new Course();
        if (productInfoFromPU != null) {
            course = setCourseInfoFromPU(course, productInfoFromPU);
            //Assumption: product code from frontend doesn't have kitted product code according to spike.
            //Spike scenario is: one user bought Mandarin level 1-5, the current product code got from customer info
            //API is a product code for one level instead of product code for level 1-5.
        } else if (productInfoFromPCM != null) {
            course = setCourseInfoFromPCM(course);
        }
        return course;
    }

    private Course setCourseInfoFromPU(Course course, ProductInfoFromUnlimited productInfoFromPU) {
        //Assumption: product code from frontend doesn't have kitted product code according to spike.
        //Spike scenario is: one user bought Mandarin level 1-5, the current product code got from customer info
        //API is a product code for one level instead of product code for level 1-5.

        Map<String, MediaSet> mediaSets = this.productInfoFromPU.getResultData().getMediaSets();
        mediaSets.forEach((currentProductCode, mediaSet) -> {
            course.setLanguageName(mediaSet.getCourseLanguageName());
            course.setLevel(mediaSet.getCourseLevel());
            transformLessonInfo(course, mediaSet);
        });

//        String languageName = "";
//        course.setLanguageName(languageName);
        return course;
    }

    private void transformLessonInfo(Course course, MediaSet mediaSet) {
        List<Lesson> lessons = new ArrayList<>();
        List<MediaItem> mediaItems = mediaSet.getMediaItems();
        for (int i = 1; i <= LESSON_NUMBER_OF_LEVEL; i++) {
            final int lessonNumber = i;

            List<MediaItem> currentLessonItems = mediaItems.stream()
                    .filter((mediaItem) -> (
                            !StringUtils.isEmpty(mediaItem.getUnit())
                            && (Integer.parseInt(mediaItem.getUnit())) == lessonNumber))
                    .collect(Collectors.toList());

            Lesson lesson = new Lesson();
            lesson.setLevel(course.getLevel());
            lesson.setLessonNumber(String.valueOf(lessonNumber));
            currentLessonItems.forEach(lessonItem -> {
                if (lessonItem.getTypeId().equals("1")) {
                    lesson.setName(lessonItem.getTitle());
                    lesson.setImage(getImage(lessonItem, mediaSet));
                    lesson.setImageDescription(lessonItem.getImageDescription());
                    lesson.setAudioLink(getAudioLink(lessonItem));
                }
            });

            lessons.add(lesson);
        }

        course.setLessons(lessons);
    }

    private Image getImage(MediaItem lessonItem, MediaSet mediaSet) {
        Image image = new Image();

        Map<String, CourseConfig> courseConfigMap = productInfoFromPU.getResultData().getCourseConfigs();
        String courseConfigKey = mediaSet.getCourseLanguageName().replace(" ", "_");
        CourseConfig courseConfig = courseConfigMap.get(courseConfigKey);
        List<CourseLevelDef> levelDefs = courseConfig.getCourseLevelDefs();

        for (int i = 0; i < levelDefs.size(); i++) {
            CourseLevelDef courseLevelDef = levelDefs.get(i);
            if (courseLevelDef.getIsbn13().equals(mediaSet.getIsbn13())) {
                String imageUrl = PREFIX_FOR_IMAGE_OF_PU + mediaSet.getCourseLanguageName().replace(" ", "").toLowerCase() + "/"
                        + courseLevelDef.getMainLessonsFullImagePath() + lessonItem.getImageURL();
                image.setFullImageAddress(imageUrl);
                break;
            }
        }

        return image;
    }

    private String getAudioLink(MediaItem lessonItem) {
        String mp3FileName = lessonItem.getFilename();
        //TODO: get full url
        //lesson.setAudioLink();  1. from product-info 2. from ***.json 3. hlfhg request
        return mp3FileName;
    }

    private Course setCourseInfoFromPCM(Course course) {
        return course;
    }
}
