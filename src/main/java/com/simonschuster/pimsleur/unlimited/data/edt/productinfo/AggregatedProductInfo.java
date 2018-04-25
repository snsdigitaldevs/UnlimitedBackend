package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregatedProductInfo {
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
            course = setCourseInfoFromPU(course);
        //Assumption: product code from frontend doesn't have kitted product code according to spike.
        //Spike scenario is: one user bought Mandarin level 1-5, the current product code got from customer info
        //API is a product code for one level instead of product code for level 1-5.
        } else if (productInfoFromPCM != null) {
            course = setCourseInfoFromPCM(course);
        }
        return course;
    }

    private Course setCourseInfoFromPU(Course course) {
        //Assumption: product code from frontend doesn't have kitted product code according to spike.
        //Spike scenario is: one user bought Mandarin level 1-5, the current product code got from customer info
        //API is a product code for one level instead of product code for level 1-5.

        Map<String, MediaSet> mediaSets = productInfoFromPU.getResultData().getMediaSets();
        mediaSets.forEach((currentProductCode, mediaSet) -> {
            course.setLanguageName(mediaSet.getCourseLanguageName());
            course.setLevel(mediaSet.getCourseLevel());
            transformLessonInfo(course, mediaSet);
        });

        String languageName = "";
        course.setLanguageName(languageName);
        return course;
    }

    private void transformLessonInfo(Course course, MediaSet mediaSet) {
        List<Lesson> lessons = new ArrayList<Lesson>();
        List<MediaItem> mediaItems = mediaSet.getMediaItems();
        for (int i= 0; i < 30; i++) {
            final int temp = i;
            List<MediaItem> currentLessonItems = mediaItems.stream()
                    .filter((mediaItem) -> Integer.parseInt(mediaItem.getUnit()) == temp).collect(Collectors.toList());

            Lesson lesson = new Lesson();
            lesson.setLevel(course.getLevel());
            lesson.setLessonNumber(String.valueOf(temp));
            currentLessonItems.forEach(lessonItem -> {
                if (lessonItem.getTypeId().equals("1")) {
                    lesson.setName(lessonItem.getTitle());
//                    lesson.setImages();
//                    lesson.setAudioLink();  1. from product-info 2. from ***.json 3. hlfhg request
                }
            });

        }

//            private List<Image> images; Add image description.
//            private String name;
//            private String audioLink;
//            private String level;
//            private String lessonNumber;

        course.setLessons(lessons);
    }

    private Course setCourseInfoFromPCM(Course course) {
        return course;
    }
}
