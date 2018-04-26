package com.simonschuster.pimsleur.unlimited.services.practices;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class AvailablePracticesServiceTest {

    @Autowired
    private AvailablePracticesService availablePracticesService;

    @Test
    public void shouldGetPracticeCsvUrls() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(
                by(uri("/subscr_production_v_9/action_handlers/hflfg.php")))
                .response(file("src/test/resources/installationFileListResponse.json"));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                PracticesCsvLocations practiceCsvLocations =
                        availablePracticesService.getPracticeCsvLocations("whatever");
                assertThat(practiceCsvLocations.getFlashCardUrl(), is("https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_FC.csv"));
                assertThat(practiceCsvLocations.getQuickMatchUrl(), is("https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_QZ.csv"));
                assertThat(practiceCsvLocations.getReadingUrl(), is("https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_RL.csv"));
                assertThat(practiceCsvLocations.getSpeakEasyUrl(), is("https://install.pimsleurunlimited.com/staging_n/mobile/mandarinchinese/Mandarin Chinese I/metadata/timecode/9781442394872_Mandarin_1_VC.csv"));
                AvailablePractices availablePractices = UnlimitedPracticeUtil.getAvailablePractices(practiceCsvLocations);
                assertThat(availablePractices.getPracticesInUnits().size(), is(30));
            }
        });
    }
}