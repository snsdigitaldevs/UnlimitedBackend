package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.*;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Image;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.unlimited.services.course.PUCourseInfoService.KEY_DOWNLOAD;
import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.isOneOfNineBig;
import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.isPuFreeLesson;
import static com.simonschuster.pimsleur.unlimited.utils.UrlUtil.encodeUrl;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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

    // factory method
    public static AggregatedProductInfo createInstanceForPcm(PcmProduct pcmProductInfo,
                                                             Map<String, List<Lesson>> pcmAudioInfo) {
        AggregatedProductInfo productInfo = new AggregatedProductInfo();
        productInfo.setPcmProduct(pcmProductInfo);
        productInfo.setPcmAudioInfo(pcmAudioInfo);
        return productInfo;
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
            course.setIsFree(isPuFreeLesson(currentProductCode));
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
            transformReadingInfoFromPU(course, mediaSet);
            courses.add(course);
        });

        return courses;
    }

    private void transformReadingInfoFromPU(Course course, MediaSet mediaSet) {
        try {
            Readings readings = new Readings();

            CourseLevelDef courseLevelDef = findCourseLevelDef(mediaSet);
            String audioBaseUrl = (PREFIX_FOR_AUDIO_OF_PU + mediaSet.getCourseLanguageName()
                    .replace(" ", "").toLowerCase() + "/") + courseLevelDef.getAudioPath();

            List<MediaItem> mediaItems = mediaSet.getMediaItems().stream()
                    .filter(MediaItem::isReadingMp3).collect(toList());

            for (MediaItem mediaItem : mediaItems) {
                String encodedAudioLink = encodeUrl(PREFIX_FOR_AUDIO_OF_PU, audioBaseUrl + mediaItem.getFilename());
                readings.getAudios().add(new ReadingAudio(parseInt(mediaItem.getUnit()), encodedAudioLink));
            }

            course.setReadings(readings);
        } catch (Exception ignored) {
            throw new PimsleurException("Error occured when convert pu reading audio links.");
        }
    }

    private void buildCourseInfoFromPCM(List<Course> courses,
                                        PcmProduct productInfoFromPCM,
                                        Map<String, List<Lesson>> lessonAudioInfoFromPCM) {
        productInfoFromPCM.getOrdersProducts().forEach((orderProductInfo) -> {

            Map<String, List<Lesson>> filteredLessonAudioInfo = lessonAudioInfoFromPCM.entrySet().stream()
                    .filter(lessonAudioInfo -> lessonAudioInfo.getValue().size() > 0)
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

            filteredLessonAudioInfo.forEach((level, lessonInfoForOneLevel) -> {
                Course course = buildPcmCourse(orderProductInfo, lessonInfoForOneLevel, level);
                courses.add(course);
            });
        });

    }

    private Course buildPcmCourse(OrdersProduct orderProductInfo, List<Lesson> lessons, String level) {
        Map<Integer, Product> products = orderProductInfo.getOrdersProductsAttributes()
                .stream()
                .filter(attr -> attr.getProductsOptions().contains(KEY_DOWNLOAD))
                .collect(toMap(it -> it.getOrdersProductsDownloads().get(0).getMediaSet().getProduct().getProductsLevel(),
                        it -> it.getOrdersProductsDownloads().get(0).getMediaSet().getProduct()));

        String languageName = orderProductInfo.getProduct().getProductsLanguageName();

        Course course = new Course();
        course.setIsOneOfNineBig(isOneOfNineBig(languageName));
        course.setLanguageName(languageName);
        course.setLevel(parseInt(level));
        course.setLessons(filterAndOrder(lessons));
        course.setCourseName(products.get(parseInt(level)).getProductsName());
        course.setProductCode(products.get(parseInt(level)).getIsbn13().replace("-", ""));

        return course;
    }

    private void transformLessonInfoFromPU(Course course, MediaSet mediaSet) {
        List<Lesson> lessons = new ArrayList<>();

        //Get media items which contains 'imageDescription', we think those items are the ones contains useful info.
        List<MediaItem> mediaItems = mediaSet.getMediaItems().stream()
                .filter((mediaItem) -> (!StringUtils.isEmpty(mediaItem.getImageURL())))
                .collect(Collectors.toList());

        mediaItems.forEach(mediaItem -> {
            Lesson lesson = new Lesson();
            lesson.setLevel(course.getLevel());
            lesson.setLessonNumber(mediaItem.getUnit());
            lesson.setName(mediaItem.getTitle());
            lesson.setCultureContent(new CultureContent(
                    deleteQuotation(mediaItem.getImageLocation()),
                    deleteQuotation(mediaItem.getImageDescription()),
                    deleteQuotation(mediaItem.getImageCredits())));
            lesson.setMediaItemId(mediaItem.getMediaItemId());
            try {
                getImageAndAudioFromPU(lesson, mediaItem, mediaSet);
            } catch (Exception e) {
                logger.error("Error occured when convert product info from PU EDT API.");
                e.printStackTrace();
                throw new PimsleurException("Error occured when convert product info from PU EDT API.");
            }

            lessons.add(lesson);
        });

        course.setLessons(filterAndOrder(lessons));
    }

    private String deleteQuotation(String origin) {
        if (origin != null && origin.startsWith("\"") && origin.endsWith("\"")) {
            origin = origin.substring(1, origin.length() - 1);
        }
        return origin;
    }

    private List<Lesson> filterAndOrder(List<Lesson> lessons) {
        return lessons.stream()
                .sorted((lesson1, lesson2) -> {
                    int seq1 = parseInt(lesson1.getLessonNumber());
                    int seq2 = parseInt(lesson2.getLessonNumber());
                    return seq1 - seq2;
                })
                .collect(Collectors.toList());
    }

    private void getImageAndAudioFromPU(Lesson lesson, MediaItem mediaItem, MediaSet mediaSet)
            throws Exception {
        CourseLevelDef courseLevelDef = findCourseLevelDef(mediaSet);
        String pathMiddlePart = mediaSet.getCourseLanguageName().replace(" ", "").toLowerCase() + "/";

        setImageAndAudio(lesson, mediaItem, courseLevelDef, pathMiddlePart);
    }

    private CourseLevelDef findCourseLevelDef(MediaSet mediaSet) throws Exception {
        String courseConfigKey = mediaSet.getCourseLanguageName().replace(" ", "_");
        CourseConfig courseConfig = puProductInfo.getResultData().getCourseConfigs().get(courseConfigKey);

        CourseLevelDef courseLevelDef = null;
        for (CourseLevelDef levelDef : courseConfig.getCourseLevelDefs()) {
            courseLevelDef = levelDef;
            if (courseLevelDef.getIsbn13().equals(mediaSet.getIsbn13())) {
                break;
            }
        }
        return courseLevelDef;
    }

    private void setImageAndAudio(Lesson lesson, MediaItem mediaItem,
                                  CourseLevelDef courseLevelDef, String middlePart)
            throws UnsupportedEncodingException {
        Image image = new Image();
        String thumbImageAddress = PREFIX_FOR_IMAGE_OF_PU + middlePart + courseLevelDef.getMainLessonsThumbImagePath() + mediaItem.getImageURL();
        String fullImageAddress = PREFIX_FOR_IMAGE_OF_PU + middlePart + courseLevelDef.getMainLessonsFullImagePath() + mediaItem.getImageURL();
        image.setFullImageAddress(encodeUrl(PREFIX_FOR_IMAGE_OF_PU, fullImageAddress));
        image.setThumbImageAddress(encodeUrl(PREFIX_FOR_IMAGE_OF_PU, thumbImageAddress));
        lesson.setImage(image);

        String audioUrl = PREFIX_FOR_AUDIO_OF_PU + middlePart + courseLevelDef.getAudioPath() + mediaItem.getFilename();
        lesson.setAudioLink(encodeUrl(PREFIX_FOR_AUDIO_OF_PU, audioUrl));
    }

    private void setPcmAudioInfo(Map<String, List<Lesson>> lessonAudioInfoFromPCM) {
        this.lessonAudioInfoFromPCM = lessonAudioInfoFromPCM;
    }
}
