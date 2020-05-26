package com.simonschuster.pimsleur.unlimited.services.freeLessons;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.services.course.PcmFreeCourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class PcmFreeLessonsServiceTest {

    @Autowired
    private PcmFreeCourseService pcmFreeCourseService;

    @Test
    public void shouldConvertFreePcmCourseFromEdtToCourseDto() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(by(uri("/subscr_production_v_9/action_handlers/nwdft.php")))
            .response(file("src/test/resources/pcmFreeCourseResponse.json"));

        running(server, () -> {
            String storeDomain = "pimsleur.com";
            List<Course> pcmFreeCourseInfos = pcmFreeCourseService.getPcmFreeCourseInfos("123", storeDomain);

            Course course = pcmFreeCourseInfos.get(0);

            assertThat(course.getLanguageName(), is("Spanish"));
            assertThat(course.getLessons().size(), is(30));
            assertThat(course.getLessons().get(0).getAudioLink().length(), greaterThan(0));
        });
    }

    @Test
    public void should_return_empty_list_when_response_data_is_null() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(by(uri("/subscr_production_v_9/action_handlers/nwdft.php")))
            .response(file("src/test/resources/pcmFreeCourseNoResponse.json"));

        running(server, () -> {
            String storeDomain = "pimsleur.com";
            List<Course> pcmFreeCourseInfos = pcmFreeCourseService.getPcmFreeCourseInfos("123", storeDomain);

            assertThat(pcmFreeCourseInfos, empty());
        });
    }
}