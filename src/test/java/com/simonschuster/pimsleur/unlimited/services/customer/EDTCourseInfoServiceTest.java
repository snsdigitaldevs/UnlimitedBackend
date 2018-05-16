package com.simonschuster.pimsleur.unlimited.services.customer;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.CourseConfig;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.MediaSet;
import com.simonschuster.pimsleur.unlimited.services.course.EDTCourseInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class EDTCourseInfoServiceTest {
    @Autowired
    private EDTCourseInfoService edtCourseInfoService;

    @Test
    public void shouldGetCorrectResponseFromEDTService() throws Exception {
        HttpServer server = mockEDTResponseFromPU();
        boolean isPUProductCode = true;

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos(isPUProductCode, "9781508243328", "");
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
                eq(form("action"), "fgtyh")
        ))
                .response(file("src/test/resources/edtProductInfoResponseWithMultipleCourses.json"));
        boolean isPUProductCode = true;

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos(isPUProductCode, "9781508260257", "");
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
    public void shouldGetProductInfoFromPCMCorrectlyWhenNoDataFromPU() throws Exception {
        HttpServer server = mockEDTResponseFromPCM();
        boolean isPUProductCode = false;
        String productCode = "9781442369030";

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos(isPUProductCode, productCode, "auth0_user_id");

            assertNull(productInfo.getPuProductInfo());
            assertNotNull(productInfo.getPcmProduct());
            assertEquals(productCode, productInfo.getPcmProduct().getOrdersProductList().get("9781442369030").getProduct().getIsbn13().replace("-", ""));
        });
    }

    @Test
    public void shouldGenerateDTOResponseCorrectlyFromPU() throws Exception {
        HttpServer httpServer = mockEDTResponseFromPU();

        running(httpServer, () -> {
            List<Course> productInfos = edtCourseInfoService.getCourseInfos(true, "9781508243328", "").toDto();
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
            assertEquals("Unit 01", lesson1InLevel1.getName());
            assertEquals("01", lesson1InLevel1.getLessonNumber());
            assertEquals("https://install.pimsleurunlimited.com/staging_n/desktop/mandarinchinese/Mandarin+Chinese+Demo/images/thumb/MA_1_M_01.jpg",
                    lesson1InLevel1.getImage().getThumbImageAddress());
        });
    }

    @Test
    public void shouldGenerateDTOResponseCorrectlyFromPCM() throws Exception {
        HttpServer server = mockEDTResponseFromPCM();
        boolean isPUProductCode = false;
        String productCode = "9781508205333";

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos(isPUProductCode, productCode, "auth0_user_id");
            List<Course> courseDtos = productInfo.toDto();
            Course levelOne = courseDtos.stream().filter(course -> course.getLevel() == 1).collect(Collectors.toList()).get(0);

            assertThat(levelOne.getHidePracticeTab(), is(false));
            assertEquals("French", levelOne.getLanguageName());

            List<Lesson> lessons = levelOne.getLessons();
            Lesson lessonOne = lessons.stream().filter(lesson -> lesson.getName().equals("Unit 01")).collect(Collectors.toList()).get(0);

            assertEquals("https://pimsleur.cdn.edtnet.us/pimsleur/subscription/9781442310223_Japanese_Phase_1/9781442310223_Unit_20.mp3?Expires=1524839994&Signature=Hw5qalqbO6a0qe4dpFcJO5xKACiYWa6kuy4pd341tFWta4yP4tZLmW4BLeHeYB4oOohcKPMn9XG8pGojntmMb37DJOyTOFaK783O5wzO5xJ7tgY-dl1fBOC1a2X9zg6CRJ-ZWtdkcK~07Ob7NSMpjBxMi3fmTxqNbD~u~61H90c_&Key-Pair-Id=APKAJRDZZRICRGT4VEOA",
                    lessonOne.getAudioLink());
            assertEquals(new Integer(1), lessonOne.getLevel());
            assertTrue(67226 == lessonOne.getMediaItemId());
            assertEquals("01", lessonOne.getLessonNumber());
            assertEquals("Unit 01", lessonOne.getName());
        });

    }

    private HttpServer mockEDTResponseFromPU() {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh"),
                eq(form("gccfs"), "[\"9781508243328\"]")))
                .response(file("src/test/resources/edtProductInfoResponse.json"));
        return server;
    }

    private HttpServer mockEDTResponseFromPCM() {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh"),
                eq(form("gccfs"), "[\"9781508243328\"]")))
                .response("{\"result_code\":-1}");

        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rsovkolfqxrjl.php")),
                eq(form("action"), "pcm_blmqide")))
                .response(file("src/test/resources/pcmCustInfoResponse.json"));

        server.post(and(
                by(uri("/subscr_dev/action_handlers/nwdft.php")),
                eq(form("action"), "slruldr")))
                .response(file("src/test/resources/pcmNewVersionAudioLinkRequest.json"));

        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rdlss.php")),
                eq(form("action"), "rdlfmix")))
                .response(file("src/test/resources/pcmAudioLinkRequest.json"));
        return server;
    }
}