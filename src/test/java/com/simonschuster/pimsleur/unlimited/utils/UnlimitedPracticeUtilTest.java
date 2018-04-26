package com.simonschuster.pimsleur.unlimited.utils;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesCsvLocations;
import org.junit.Test;

import java.io.IOException;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UnlimitedPracticeUtilTest {

    @Test
    public void shouldIgnoreDuplicateHeaderInCSV() throws Exception {
        PracticesCsvLocations practicesCsvLocations = new PracticesCsvLocations();
        practicesCsvLocations.setSpeakEasyUrl("http://localhost:12306/hello.csv");

        HttpServer server = httpServer(12306);
        server.get(by(uri("/hello.csv")))
                .response("\"a\",\"b\",\"c\",\"c\",\"Unit Num\"" + System.lineSeparator() + "1,2,3,4,5");

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                AvailablePractices availablePractices = UnlimitedPracticeUtil.getAvailablePractices(practicesCsvLocations);
                assertThat(availablePractices.getPracticesInUnits().size(), is(1));
                assertThat(availablePractices.getPracticesInUnits().get(0).getUnitNumber(), is(5));
            }
        });
    }
}