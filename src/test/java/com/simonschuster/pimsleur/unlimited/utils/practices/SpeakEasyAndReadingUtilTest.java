package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@TestPropertySource("classpath:test-config.properties")
public class SpeakEasyAndReadingUtilTest {

    @Test
    public void shouldConvertCsvFileToSpeakEasies() throws Exception {

        HttpServer server = httpServer(12306);
        server.get(by(uri("/xxx.csv")))
                .response(file("src/test/resources/csv/9781442394872_Mandarin_1_VC.csv"));

        running(server, () -> {
            List<PracticesInUnit> practicesInUnits = SpeakEasyAndReadingUtil.csvToSpeakEasies("http://localhost:12306/xxx.csv");
            assertThat(practicesInUnits.size(), is(30));
        });
    }

    @Test
    public void shouldConvertCsvFileToReadings() throws Exception {

        HttpServer server = httpServer(12306);
        server.get(by(uri("/xxx.csv")))
                .response(file("src/test/resources/csv/9781508222033_Japanese_1_RL.csv"));

        running(server, () -> {
            List<PracticesInUnit> practicesInUnits = SpeakEasyAndReadingUtil.csvToReadings("http://localhost:12306/xxx.csv");
            assertThat(practicesInUnits.size(), is(20));
        });
    }

}