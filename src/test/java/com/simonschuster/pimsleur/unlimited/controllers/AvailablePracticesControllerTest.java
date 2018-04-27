package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AvailablePracticesControllerTest {

    List<String> puISBNs = asList(
//            "9781442394872", "9781508231431", "9781442394889", "9781508260257",
//            "9781442394896", "9781442394902", "9781442383265", "9781508231424",
//            "9781508230823", "9781508227939",
            "9781508235927", "9781508235934",
            "9781508260233", "9781508235941", "9781508227946", "9781442348646",
            "9781508231165", "9781442350328", "9781508260141", "9781442350335",
            "9781442370555", "9781442368439", "9781508231370", "9781442396173",
            "9781508227847", "9781442348653", "9781508231387", "9781442350342",
            "9781508260189", "9781442350359", "9781442348684", "9781442370562",
            "9781442369535", "9781508231394", "9781508230892", "9781508227878",
            "9781508231400", "9781442350366", "9781508260202", "9781442350373",
            "9781442370579", "9781442367869", "9781508231417", "9781508230922",
            "9781442396180", "9781508222033", "9781508222040", "9781508260240",
            "9781508222057", "9781508222064", "9781508216780", "9781508235972",
            "9781442348660", "9781508235989", "9781442374980", "9781442373723",
            "9781508231448", "9781442374997", "9781508260264", "9781442375000",
            "9781508235842", "9781508227915", "9781442382848", "9781508231486",
            "9781442382831", "9781508260226", "9781442384293", "9781442381841",
            "9781508231493", "9781508236030", "9781508236023", "9781442346345",
            "9781508231509", "9781442350380", "9781508260219", "9781442350397",
            "9781442348707", "9781442350403", "9781442348714", "9781508231516",
            "9781508231455", "978150823094", "9781508227786");

    @Autowired
    private AvailablePracticesController availablePracticesController;

    @Test
    public void shouldGetAvailablePracticesOfAllPuISBNs() throws Exception {
        for (String puISBN : puISBNs) {
            System.out.println(puISBNs.indexOf(puISBN));
            System.out.println(puISBN + " will run");
            AvailablePractices puAvailablePractices = availablePracticesController
                    .getPuAvailablePractices(puISBN);
            System.out.println(puISBN + " is ok");
        }
    }

}