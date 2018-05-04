package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import org.junit.Test;

import java.util.List;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SpeakEasyUtilTest {
    @Test
    public void shouldConvertCsvFileToSpeakEasies() throws Exception {

        HttpServer server = httpServer(12306);
        server.get(by(uri("/xxx.csv")))
                .response(file("src/test/resources/csv/9781442394872_Mandarin_1_VC.csv"));

        running(server, () -> {
            List<PracticesInUnit> practicesInUnits = SpeakEasyUtil.csvToSpeakEasies("http://localhost:12306/xxx.csv");
            assertThat(practicesInUnits.size(), is(30));
        });
    }
}