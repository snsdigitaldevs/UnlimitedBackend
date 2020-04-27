package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.CultureContent;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Image;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.ReadingAudio;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Readings;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProductAttribute;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Product;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.isOneOfNineBig;
import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.isPuFreeLesson;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class AggregatedProductInfo {
    private static final String THUMB = "/images/thumb/";
    private static final String FULL = "/images/full/";

    private static final Logger logger = LoggerFactory.getLogger(AggregatedProductInfo.class);

    private PuProductInfo puProductInfo;
    private InstallationFileList installationFileList;
    private PcmProduct pcmProduct;
    private List<Course> lessonAudioInfoFromPCM;

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

    public InstallationFileList getInstallationFileList() {
        return installationFileList;
    }

    public void setInstallationFileList(InstallationFileList installationFileList) {
        this.installationFileList = installationFileList;
    }

    // factory method
    public static AggregatedProductInfo createInstanceForPcm(PcmProduct pcmProductInfo,
                                                             List<Course> pcmAudioInfo) {
        AggregatedProductInfo productInfo = new AggregatedProductInfo();
        productInfo.setPcmProduct(pcmProductInfo);
        productInfo.setPcmAudioInfo(pcmAudioInfo);
        return productInfo;
    }

    public List<Course> toDto() {
        ArrayList<Course> courses = new ArrayList<>();

        if (puProductInfo != null && puProductInfo.getResultData() != null) {
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
                logger.error("Error occured when get product name from PU EDT API.", e);
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

            List<ReadingAudio> readingAudios = getReadingAudiosInfo(mediaSet);
            String puReadingAlphabetPdfUrl = getReadingAlphabetPdfUrl(mediaSet);
            String puReadingIntroPdfUrl = getReadingIntroPdfUrl(mediaSet);

            readings.setAudios(readingAudios);
            readings.setPuReadingAlphabetPdf(puReadingAlphabetPdfUrl);
            readings.setPuReadingIntroPdf(puReadingIntroPdfUrl);
            course.setReadings(readings);
        } catch (Exception e) {
            logger.error("Error occured when convert pu reading audio links.", e);
            throw new PimsleurException("Error occured when convert pu reading audio links.");
        }
    }

    private String getReadingIntroPdfUrl(MediaSet mediaSet) {
        List<MediaItem> mediaItems = mediaSet.getMediaItems()
                        .stream().filter(MediaItem::isReadingIntroduction).collect(toList());
        if(!mediaItems.isEmpty()) {
            String readingIntroPdfName = mediaItems.get(0).getFilename();
            return installationFileList.getReadingPdfUrlByFileName(readingIntroPdfName);
        }
        return null;
    }

    private String getReadingAlphabetPdfUrl(MediaSet mediaSet) {
        List<MediaItem> mediaItems = mediaSet.getMediaItems()
                        .stream().filter(MediaItem::isReadingAlphabet).collect(toList());
        if(!mediaItems.isEmpty()) {
            String readingAlphabetPdfName = mediaItems.get(0).getFilename();
            return this.installationFileList.getReadingPdfUrlByFileName(readingAlphabetPdfName);
        }
        return null;
    }


    private List<ReadingAudio> getReadingAudiosInfo(MediaSet mediaSet) {
        List<MediaItem> mediaItems = mediaSet.getMediaItems().stream()
                .filter(MediaItem::isReadingMp3).collect(toList());
        List<ReadingAudio> readingAudios = new ArrayList<ReadingAudio>();
        for (MediaItem mediaItem : mediaItems) {
            String encodedAudioLink = installationFileList.getUrlByFileName(mediaItem.getFilename());
            readingAudios.add(new ReadingAudio(parseInt(mediaItem.getUnit()), encodedAudioLink));
        }
        return readingAudios;
    }

    private void buildCourseInfoFromPCM(List<Course> courses,
                                        PcmProduct productInfoFromPCM,
                                        List<Course> lessonAudioInfoFromPCM) {
        productInfoFromPCM.getOrdersProducts().forEach((orderProductInfo) -> {
            List<Course> filteredLessonAudioInfo = lessonAudioInfoFromPCM.stream()
                    .filter(course -> course.getLessons().size() > 0)
                    .collect(Collectors.toList());

            filteredLessonAudioInfo.forEach(courseWithLessonInfoOnly -> {
                Course course = buildPcmCourse(orderProductInfo, courseWithLessonInfoOnly);
                courses.add(course);
            });
        });

    }

    private Course buildPcmCourse(OrdersProduct orderProductInfo, Course courseWithLessonInfoOnly) {
        List<Lesson> lessons = courseWithLessonInfoOnly.getLessons();
        assignImageForPcmLessons(orderProductInfo, lessons);

        String level = String.valueOf(courseWithLessonInfoOnly.getLevel());
        Map<Integer, Product> products = orderProductInfo.getOrdersProductsAttributes()
                .stream()
                .filter(OrdersProductAttribute::isDownload)
                .collect(toMap(it -> it.getOrdersProductsDownloads().get(0).getMediaSet().getProduct().getProductsLevel(),
                        it -> it.getOrdersProductsDownloads().get(0).getMediaSet().getProduct()));

        Course course = new Course();

        String languageName = orderProductInfo.getProduct().getProductsLanguageName();
        course.setIsOneOfNineBig(isOneOfNineBig(languageName));
        course.setLanguageName(languageName);

        course.setLevel(parseInt(level));
        course.setLessons(filterAndOrder(lessons));

        Product product = products.get(parseInt(level));
        course.setCourseName(product.getProductsName());
        course.setProductCode(product.getIsbn13().replace("-", ""));

        return course;
    }

    private void assignImageForPcmLessons(OrdersProduct orderProductInfo, List<Lesson> lessons) {
        Image pcmImage = orderProductInfo.getProduct().getPcmImage();
        lessons.forEach(lesson -> lesson.setImage(pcmImage));
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
                getImageAndAudioFromPU(lesson, mediaItem);
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

    private void getImageAndAudioFromPU(Lesson lesson, MediaItem mediaItem) {
        setImageAndAudio(lesson, mediaItem);
    }

    private void setImageAndAudio(Lesson lesson, MediaItem mediaItem) {
        Image image = new Image();
        String thumbImageName = THUMB + mediaItem.getImageURL();
        String fullImageName = FULL + mediaItem.getImageURL();
        image.setThumbImageAddress(installationFileList.getUrlByFileName(thumbImageName));
        image.setFullImageAddress(installationFileList.getUrlByFileName(fullImageName));
        lesson.setImage(image);

        lesson.setAudioLink(installationFileList.getUrlByFileName(mediaItem.getFilename()));
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

    private void setPcmAudioInfo(List<Course> lessonAudioInfoFromPCM) {
        this.lessonAudioInfoFromPCM = lessonAudioInfoFromPCM;
    }
}
