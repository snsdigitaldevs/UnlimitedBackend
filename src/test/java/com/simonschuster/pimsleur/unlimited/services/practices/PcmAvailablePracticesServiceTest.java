package com.simonschuster.pimsleur.unlimited.services.practices;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class PcmAvailablePracticesServiceTest {

    @Autowired
    private PcmAvailablePracticesService pcmAvailablePracticesService;

    @Test
    public void shouldGetAvailablePCMReadingsForOneTimePurchase() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rsovkolfqxrjl.php")),
                eq(form("action"), "pcm_blmqide")))
                .response(file("src/test/resources/pcmCustInfoResponse.json"));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                AvailablePractices availablePractices = pcmAvailablePracticesService
                        .getAvailablePractices("9781442307674", "sub of auth0", "email");

                List<Integer> unitsWithReadings = availablePractices.getPracticesInUnits().stream()
                        .map(PracticesInUnit::getUnitNumber)
                        .collect(toList());
                assertThat(unitsWithReadings.size(), is(22));
            }
        });
    }

    @Test
    public void shouldGetAvailablePCMReadingsForSubscription() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rsovkolfqxrjl.php")),
                eq(form("action"), "pcm_blmqide")))
                .response(file("src/test/resources/pcmCustInfoWithSubscription.json"));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                AvailablePractices availablePractices = pcmAvailablePracticesService
                        .getAvailablePractices("9781442308046", "sub of auth0", "email");

                List<Integer> unitsWithReadings = availablePractices.getPracticesInUnits().stream()
                        .map(PracticesInUnit::getUnitNumber)
                        .collect(toList());
                assertThat(unitsWithReadings.size(), is(20));
            }
        });
    }
}