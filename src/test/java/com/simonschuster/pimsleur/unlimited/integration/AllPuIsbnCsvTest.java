package com.simonschuster.pimsleur.unlimited.integration;

import com.simonschuster.pimsleur.unlimited.controllers.AvailablePracticesController;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.simonschuster.pimsleur.unlimited.integration.PUIsbnList.puISBNs;

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
            System.out.println(puISBNs.indexOf(puISBN) + 1);
            System.out.println(puISBN + " will run");
            availablePracticesController.getPuAvailablePractices(puISBN);
            System.out.println(puISBN + " is ok");
        }
    }

}
