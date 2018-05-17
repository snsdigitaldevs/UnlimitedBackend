package com.simonschuster.pimsleur.unlimited.integration;

import com.simonschuster.pimsleur.unlimited.controllers.AvailablePracticesController;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.FlashCard;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.simonschuster.pimsleur.unlimited.integration.PUIsbnList.puISBNs;
import static java.lang.System.out;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AllPuIsbnCsvTest {
    @Autowired
    private AvailablePracticesController availablePracticesController;

    @Test
    @Ignore // do not run this test in ci, we only need this to run locally
    // to see if some csv files would cause errors
    public void shouldGetAvailablePracticesOfAllPuISBNs() throws Exception {
        for (String puISBN : puISBNs) {

            out.println(puISBNs.indexOf(puISBN) + 1);
            out.println(puISBN + " will run");

            AvailablePractices puAvailablePractices = availablePracticesController.getPuAvailablePractices(puISBN);
            verifyFlashCardMp3FileName(puAvailablePractices);

            out.println(puISBN + " is ok");
        }
    }

    private void verifyFlashCardMp3FileName(AvailablePractices puAvailablePractices) {
        puAvailablePractices.getPracticesInUnits().stream()
                .flatMap(practicesInUnit -> practicesInUnit.getFlashCards().stream())
                .map(FlashCard::getMp3FileName)
                .forEach(flashCardMp3FileName -> {
                    assertThat(flashCardMp3FileName, containsString(".mp3"));
                });
    }

}
