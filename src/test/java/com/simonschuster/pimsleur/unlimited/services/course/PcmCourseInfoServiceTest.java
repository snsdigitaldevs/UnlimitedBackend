package com.simonschuster.pimsleur.unlimited.services.course;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo;
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
public class PcmCourseInfoServiceTest {

    @Autowired
    private PcmCourseInfoService pcmCourseInfoService;

    @Test
    public void shouldGenerateDTOResponseCorrectlyFromPCM() throws Exception {
        HttpServer server = mockEDTResponseFromPCM();
        String productCode = "9781508205333";

        running(server, () -> {
            List<Course> courseDtos = pcmCourseInfoService.getCourses(productCode, "sub");

            Course levelOne = courseDtos.stream()
                    .filter(course -> course.getLevel() == 1).collect(Collectors.toList()).get(0);

            assertEquals("French", levelOne.getLanguageName());
            assertThat(levelOne.getIsOneOfNineBig(), is(true));

            List<Lesson> lessons = levelOne.getLessons();
            Lesson lessonOne = lessons.stream().filter(lesson -> lesson.getName().equals("Lesson 01")).collect(Collectors.toList()).get(0);

            assertEquals("https://pimsleur.cdn.edtnet.us/pimsleur/subscription/9781442310223_Japanese_Phase_1/9781442310223_Unit_20.mp3?Expires=1524839994&Signature=Hw5qalqbO6a0qe4dpFcJO5xKACiYWa6kuy4pd341tFWta4yP4tZLmW4BLeHeYB4oOohcKPMn9XG8pGojntmMb37DJOyTOFaK783O5wzO5xJ7tgY-dl1fBOC1a2X9zg6CRJ-ZWtdkcK~07Ob7NSMpjBxMi3fmTxqNbD~u~61H90c_&Key-Pair-Id=APKAJRDZZRICRGT4VEOA",
                    lessonOne.getAudioLink());
            assertEquals(new Integer(1), lessonOne.getLevel());
            assertTrue(67226 == lessonOne.getMediaItemId());
            assertEquals("01", lessonOne.getLessonNumber());
            assertEquals("Lesson 01", lessonOne.getName());
        });
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