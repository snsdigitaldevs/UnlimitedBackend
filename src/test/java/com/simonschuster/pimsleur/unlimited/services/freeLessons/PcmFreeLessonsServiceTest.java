package com.simonschuster.pimsleur.unlimited.services.freeLessons;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class PcmFreeLessonsServiceTest {

    @Autowired
    private PcmFreeLessonsService pcmFreeLessonsService;

    @Test
    public void shouldGetOnlyLevelOneFromEachLanguageAndIgnoreSubscriptions() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(by(uri("/subscr_production_v_9/action_handlers/kmuhtr.php")))
                .response(file("src/test/resources/pcmProducts.json"));

        running(server, () -> {
            List<AvailableProductDto> pcmFreeLessons = pcmFreeLessonsService.getPcmFreeLessons();
            assertThat(pcmFreeLessons.size(), is(57));
        });
    }
}