package com.simonschuster.pimsleur.unlimited.services.customer;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
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
public class EDTCourseInfoServiceTest {
    @Autowired
    private EDTCourseInfoService edtCourseInfoService;

    @Test
    public void shouldGetCorrectResponseFromEDTService() throws Exception {
        HttpServer server = mockEDTResponseFromPU();
        boolean isPUProductCode = true;

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos(isPUProductCode, "9781508243328", "");
            assertThat(productInfo.getProductInfoFromPU().getResultCode(),is(1));
            assertNotNull(productInfo.getProductInfoFromPU().getResultData().getCourseConfigs());
            assertNotNull(productInfo.getProductInfoFromPU().getResultData().getMediaSets());

            CourseConfig courseConfig = productInfo.getProductInfoFromPU().getResultData().getCourseConfigs().get("Mandarin_Chinese");
            assertEquals("1.0", courseConfig.getVersion());
            assertEquals("Mandarin Chinese", courseConfig.getCourseLangName());
            assertNotNull(courseConfig.getAppDefines());
            assertEquals("Show Pinyin Pronunciation Guide", courseConfig.getReadingLessonConfig().getShowAlphabetChartLabel());
            assertEquals("Pimsleur Mandarin Chinese Unlimited Level 1 Sample", courseConfig.getIsbnToCourseName().get("9781508243328"));
            assertEquals(3, courseConfig.getKittedFileLists().size());
            assertEquals(new Integer(1), courseConfig.getCourseLevelDefs().get(0).getIsDemo());

            MediaSet oneMediaSet = productInfo.getProductInfoFromPU().getResultData().getMediaSets().get("9781508243328");
            assertEquals("9781508243328_Mandarin_1_AD.csv", oneMediaSet.getMediaItems().get(0).getFilename());
            //Test Jackson convert between String and Integer, source data has two types in different places.
            assertEquals(new Integer(1), oneMediaSet.getMediaItems().get(0).getIsActive());
            assertEquals(new Integer(1), oneMediaSet.getMediaItems().get(6).getIsActive());
            assertEquals("0", oneMediaSet.getMediaItems().get(2).getFileSizeBytes());
            assertEquals("", oneMediaSet.getMediaItems().get(0).getFileSizeBytes());
        });
    }

    //TODO: need to confirm if the courseconfigs part of response for multiple courses is the same as the test resource file.
    @Test
    public void shouldGetCorrectResponseFromEDTServiceWithMultipleCourse() throws Exception {
        //mock edt api response
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh")
                ))
                .response(file("src/test/resources/edtProductInfoResponseWithMultipleCourses.json"));
        boolean isPUProductCode = true;

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos(isPUProductCode, "whatever", "");
            assertThat(productInfo.getProductInfoFromPU().getResultCode(),is(1));
            assertNotNull(productInfo.getProductInfoFromPU().getResultData().getCourseConfigs());
            assertEquals(2, productInfo.getProductInfoFromPU().getResultData().getCourseConfigs().size());
        });
    }

    @Test
    public void shouldGetProductInfoFromPCMCorrectlyWhenNoDataFromPU() throws Exception {
        HttpServer server = mockEDTResponseFromPCM();
        boolean isPUProductCode = false;
        String productCode = "9781442369030";

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos(isPUProductCode, productCode, "auth0_user_id");

            assertNull(productInfo.getProductInfoFromPU());
            assertNotNull(productInfo.getProductInfoFromPCM());
            assertEquals(productCode, productInfo.getProductInfoFromPCM().getOrderProduct().getProduct().getIsbn13().replace("-", ""));
        });
    }

    @Test
    public void shouldGenerateDTOResponseCorrectlyFromPU() throws Exception {
        HttpServer httpServer = mockEDTResponseFromPU();

        running(httpServer, () -> {
            List<Course> productInfos = edtCourseInfoService.getCourseInfos(true, "9781508243328", "").toDto();
            Course productInfo = productInfos.get(0);

            assertEquals("Mandarin Chinese", productInfo.getLanguageName());

            assertEquals("https://install.pimsleurunlimited.com/staging_n/desktop/mandarinchinese"
                    + "/Mandarin Chinese Demo/images/full/MA_1_M_01.jpg",
                    productInfo.getLessons().get(0).getImage().getFullImageAddress());

            assertEquals("https://install.pimsleurunlimited.com/staging_n/common/mandarinchinese"
                            + "/Mandarin Chinese Demo/audio/9781508243328_Mandarin_Chinese1_U01_Lesson.mp3",
                    productInfo.getLessons().get(0).getAudioLink());

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
            Course courseDto = courseDtos.get(0);

            assertEquals("French", courseDto.getLanguageName());
            assertEquals(5, productInfo.getMediaSetInfo().size());
//            assertEquals("", courseDto.getLevel());
//            assertTrue(courseDto.getLessons().get(0).getAudioLink().contains("https://pimsleur.cdn.edtnet.us/pimsleur/subscription/9781442310223_Japanese_Phase_1/9781442310223_Unit_01.mp3"));
            //value of Expires and Signature is dynamic
//            assertTrue(courseDto.getLessons().get(0).getAudioLink().contains("Expires="));
//            assertTrue(courseDto.getLessons().get(0).getAudioLink().contains("Signature="));
//            assertTrue(courseDto.getLessons().get(0).getAudioLink().contains("Key-Pair-Id=APKAJRDZZRICRGT4VEOA"));
//            assertEquals("42578", courseDto.getLessons().get(0).getMediaItemId());
        });

    }

    private HttpServer mockEDTResponseFromPU() {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh"),
                eq(form("gccfs"), "[\"9781508243328\"]")
        ))
                .response(file("src/test/resources/edtProductInfoResponse.json"));
        return server;
    }

    private HttpServer mockEDTResponseFromPCM() {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh"),
                eq(form("gccfs"), "[\"9781508243328\"]")
        ))
                .response("{\"result_code\":-1}");

        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rsovkolfqxrjl.php")),
                eq(form("action"), "pcm_blmqide"))
        )
                .response(file("src/test/resources/pcmCustInfoResponse.json"));
        return server;
    }
}