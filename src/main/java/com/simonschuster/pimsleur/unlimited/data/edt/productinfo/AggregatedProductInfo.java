package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Image;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Product;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCourseInfoService;
import com.simonschuster.pimsleur.unlimited.utils.UrlUtil;
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
            course.setProductCode(currentProductCode);
            try {
                course.setCourseName(this.puProductInfo
                        .getResultData()
                        .getCourseConfigs()
                        .get(mediaSet.getCourseLanguageName().replace(" ", "_"))
                        .getIsbnToCourseName()
                        .get(course.getProductCode()));
            } catch (Exception e) {
                logger.error("Error occured when get product name from PU EDT API.");
                e.printStackTrace();
                throw new PimsleurException("Error occured when get product name from PU EDT API.");
            }
            transformLessonInfoFromPU(course, mediaSet);
            courses.add(course);
        });

        return courses;
    }

    private List<Course> buildCourseInfoFromPCM(List<Course> courses, PcmProduct productInfoFromPCM, Map<String, List<Lesson>> lessonAudioInfoFromPCM) {

        productInfoFromPCM.getOrdersProductList().forEach((orderProductCode, orderProductInfo) -> {
            Map<Integer, Product> products = productInfoFromPCM.getOrdersProductList().get(orderProductCode).getOrdersProductsAttributes()
                    .stream()
                    .filter(attr -> attr.getProductsOptions().contains(EDTCourseInfoService.KEY_DOWNLOAD))
                    .collect(Collectors.toMap(it -> it.getOrdersProductsDownloads().get(0).getMediaSet().getProduct().getProductsLevel(),
                            it -> it.getOrdersProductsDownloads().get(0).getMediaSet().getProduct()));

            if (lessonAudioInfoFromPCM.size() != 0 && lessonAudioInfoFromPCM.get("1").size() == 0 && products.get(0) != null) {
                List<Lesson> lessons = lessonAudioInfoFromPCM.get("1");
                String level = "0";
                Course course = buildCourse(orderProductInfo, products, lessons, level);
                courses.add(course);
            } else {
                lessonAudioInfoFromPCM.forEach((level, lessonInfoForOneLevel) -> {
                    Course course = buildCourse(orderProductInfo, products, lessonInfoForOneLevel, level);
                    courses.add(course);
                });
            }
        });

        return courses;
    }

    private Course buildCourse(OrdersProduct orderProductInfo, Map<Integer, Product> products, List<Lesson> lessons, String level) {
        Course course = new Course();
        course.setLanguageName(orderProductInfo.getProduct().getProductsLanguageName());
        course.setLevel(Integer.parseInt(level));
        course.setLessons(filterAndOrder(lessons));

        course.setCourseName(products.get(Integer.parseInt(level)).getProductsName());
        course.setProductCode(products.get(Integer.parseInt(level)).getIsbn13().replace("-", ""));

        return course;
    }

    private void transformLessonInfoFromPU(Course course, MediaSet mediaSet) {
        List<Lesson> lessons = new ArrayList<>();

        //Get media items which contains 'imageDescription', we think those items are the ones contains useful info.
        List<MediaItem> mediaItems = mediaSet.getMediaItems().stream()
                .filter((mediaItem) -> (!StringUtils.isEmpty(mediaItem.getImageURL())))
                .collect(Collectors.toList());

        mediaItems.forEach(lessonItem -> {
            Lesson lesson = new Lesson();
            lesson.setLevel(course.getLevel());
            lesson.setLessonNumber(lessonItem.getUnit());
            lesson.setName(lessonItem.getTitle());
            lesson.setImageDescription(lessonItem.getImageDescription());
            lesson.setMediaItemId(lessonItem.getMediaItemId());
            try {
                getImageAndAudioFromPU(lesson, lessonItem, mediaSet);
            } catch (Exception e) {
                logger.error("Error occured when convert product info from PU EDT API.");
                e.printStackTrace();
                throw new PimsleurException("Error occured when convert product info from PU EDT API.");
            }

            lessons.add(lesson);
        });


        course.setLessons(filterAndOrder(lessons));
    }

    private List<Lesson> filterAndOrder(List<Lesson> lessons) {
        return lessons.stream()
                .filter(lesson -> lesson.getName().startsWith("Unit")|lesson.getName().startsWith("Lesson"))
                .sorted((lesson1, lesson2) -> {
                    int seq1 = Integer.parseInt(lesson1.getLessonNumber());
                    int seq2 = Integer.parseInt(lesson2.getLessonNumber());
                    return seq1 - seq2;
                })
                .collect(Collectors.toList());
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

                image.setFullImageAddress(UrlUtil.encodeUrl(PREFIX_FOR_IMAGE_OF_PU, fullImageAddress));
                image.setThumbImageAddress(UrlUtil.encodeUrl(PREFIX_FOR_IMAGE_OF_PU, thumbImageAddress));
                break;
            }
        }

        lesson.setImage(image);
        lesson.setAudioLink(UrlUtil.encodeUrl(PREFIX_FOR_AUDIO_OF_PU, audioUrl));
    }

    public void setPcmAudioInfo(Map<String, List<Lesson>> lessonAudioInfoFromPCM) {
        this.lessonAudioInfoFromPCM = lessonAudioInfoFromPCM;
    }
}
