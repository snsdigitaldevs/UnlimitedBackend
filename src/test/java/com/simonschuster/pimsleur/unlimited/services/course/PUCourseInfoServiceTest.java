package com.simonschuster.pimsleur.unlimited.services.course;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.CourseConfig;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.MediaSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class PUCourseInfoServiceTest {

    @Autowired
    private PUCourseInfoService puCourseInfoService;
    private String storeDomain = "pimsleur.com";

    @Test
    public void shouldGetCorrectResponseFromEDTService() throws Exception {
        HttpServer server = mockEDTResponseFromPU();

        running(server, () -> {
            AggregatedProductInfo productInfo = puCourseInfoService.getPuProductInfo("9781508243328", storeDomain);

            assertThat(productInfo.getPuProductInfo().getResultCode(), is(1));
            assertNotNull(productInfo.getPuProductInfo().getResultData().getCourseConfigs());
            assertNotNull(productInfo.getPuProductInfo().getResultData().getMediaSets());

            CourseConfig courseConfig = productInfo.getPuProductInfo().getResultData().getCourseConfigs().get("Mandarin_Chinese");
            assertEquals("1.0", courseConfig.getVersion());
            assertEquals("Mandarin Chinese", courseConfig.getCourseLangName());
            assertNotNull(courseConfig.getAppDefines());
            assertEquals("Show Pinyin Pronunciation Guide", courseConfig.getReadingLessonConfig().getShowAlphabetChartLabel());
            assertEquals("Pimsleur Mandarin Chinese Unlimited Level 1 Sample", courseConfig.getIsbnToCourseName().get("9781508243328"));
            assertEquals(3, courseConfig.getKittedFileLists().size());
            assertEquals(new Integer(1), courseConfig.getCourseLevelDefs().get(0).getIsDemo());

            MediaSet oneMediaSet = productInfo.getPuProductInfo().getResultData().getMediaSets().get("9781508243328");
            assertEquals("9781508243328_Mandarin_1_AD.csv", oneMediaSet.getMediaItems().get(0).getFilename());
            //Test Jackson convert between String and Integer, source data has two types in different places.
            assertEquals(new Integer(1), oneMediaSet.getMediaItems().get(0).getIsActive());
            assertEquals(new Integer(1), oneMediaSet.getMediaItems().get(6).getIsActive());
            assertEquals("0", oneMediaSet.getMediaItems().get(2).getFileSizeBytes());
            assertEquals("", oneMediaSet.getMediaItems().get(0).getFileSizeBytes());
        });
    }

    @Test
    public void shouldGetCorrectProductInfoFromPUWithKittedProductCode() throws Exception {
        //mock edt api response
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh")))
                .response(file("src/test/resources/edtProductInfoResponseWithMultipleCourses.json"));
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/hflfg.php")),
                eq(form("action"), "gfl")))
                .response(file("src/test/resources/installationFileListResponse.json"));

        running(server, () -> {
            AggregatedProductInfo productInfo = puCourseInfoService.getPuProductInfo("9781508260257", storeDomain);

            assertThat(productInfo.getPuProductInfo().getResultCode(), is(1));
            assertNotNull(productInfo.getPuProductInfo().getResultData().getCourseConfigs());
            assertEquals(1, productInfo.getPuProductInfo().getResultData().getCourseConfigs().size());
            assertEquals(2, productInfo.getPuProductInfo().getResultData().getMediaSets().size());

            List<Course> courses = productInfo.toDto();
            assertEquals(2, courses.size());
            assertEquals(new Integer(1), courses.get(0).getLevel());
            assertEquals(new Integer(2), courses.get(1).getLevel());
            Lesson lesson1InLevel1 = courses.get(0).getLessons().get(0);
            assertEquals(new Integer(330006), lesson1InLevel1.getMediaItemId());
            assertEquals("https://install.pimsleurunlimited.com/staging_n/desktop/mandarinchinese/Mandarin+Chinese+I/images/thumb/MA_1_M_01.jpg",
                    lesson1InLevel1.getImage().getThumbImageAddress());
        });
    }

    @Test
    public void shouldGenerateDTOResponseCorrectlyFromPU() throws Exception {
        HttpServer httpServer = mockEDTResponseFromPU();

        running(httpServer, () -> {
            AggregatedProductInfo productInfo = puCourseInfoService.getPuProductInfo("9781508243328", storeDomain);

            List<Course> productInfos = productInfo.toDto();
            Course course = productInfos.get(0);

            assertEquals("Mandarin Chinese", course.getLanguageName());

            Lesson lesson1InLevel1 = course.getLessons().get(0);
            assertEquals("https://install.pimsleurunlimited.com/staging_n/desktop/mandarinchinese"
                            + "/Mandarin+Chinese+Demo/images/full/MA_1_M_01.jpg",
                    lesson1InLevel1.getImage().getFullImageAddress());

            assertEquals("https://install.pimsleurunlimited.com/staging_n/common/mandarinchinese"
                            + "/Mandarin+Chinese+Demo/audio/9781508243328_Mandarin_Chinese1_U01_Lesson.mp3",
                    lesson1InLevel1.getAudioLink());
            assertEquals(new Integer(331006), lesson1InLevel1.getMediaItemId());
            assertEquals("Lesson 1", lesson1InLevel1.getName());
            assertEquals("01", lesson1InLevel1.getLessonNumber());
            assertEquals("https://install.pimsleurunlimited.com/staging_n/desktop/mandarinchinese/Mandarin+Chinese+Demo/images/thumb/MA_1_M_01.jpg",
                    lesson1InLevel1.getImage().getThumbImageAddress());
        });
    }

    private HttpServer mockEDTResponseFromPU() {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh"),
                eq(form("gccfs"), "[\"9781508243328\"]")))
                .response(file("src/test/resources/edtProductInfoResponse.json"));
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/hflfg.php")),
                eq(form("action"), "gfl"),
                eq(form("nbsi"), "9781508243328")))
                .response(file("src/test/resources/installationFileListResponse.json"));
        return server;
    }

}
