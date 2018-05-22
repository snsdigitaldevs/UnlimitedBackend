package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesUrls;
import org.junit.Test;

import java.util.List;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static com.simonschuster.pimsleur.unlimited.utils.practices.FlashCardUtil.csvToFlashCards;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FlashCardUtilTest {

    @Test
    public void shouldConvertCsvFileToFlashCards() throws Exception {

        HttpServer server = httpServer(12306);
        server.get(by(uri("/xxx.csv")))
                .response(file("src/test/resources/csv/9781442394872_Mandarin_1_FC.csv"));

        running(server, () -> {
            PracticesUrls practicesUrls = new PracticesUrls();
            practicesUrls.setFlashCardUrl("http://localhost:12306/xxx.csv");

            List<PracticesInUnit> practicesInUnits = csvToFlashCards(practicesUrls);

            assertThat(practicesInUnits.size(), is(30));
        });
    }
}