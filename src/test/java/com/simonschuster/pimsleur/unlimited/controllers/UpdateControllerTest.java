package com.simonschuster.pimsleur.unlimited.controllers;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UpdateControllerTest extends TestCase {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Ignore
    public void should_return_true_when_version_is_2_16() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/checkUpdate").param("version", "2.16").param("storeDomain", "android_inapp").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.releaseNote").value("Update app skills"))
                .andExpect(jsonPath("$.hasUpdate").value(true))
                .andExpect(jsonPath("$.updateURL").value("http://www.google.com"))
                .andExpect(jsonPath("$.title").value("A new version is now available"))
                .andExpect(jsonPath("$.forceUpdate").value(true))
                .andExpect(jsonPath("$.latestVersion").value(2.18));
    }

    @Test
    @Ignore
    public void should_return_true_when_version_is_2_8() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/checkUpdate").param("version", "2.8").param("storeDomain", "ios_inapp").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.releaseNote").value("Update app skills"))
                .andExpect(jsonPath("$.hasUpdate").value(true))
                .andExpect(jsonPath("$.title").value("A new version is now available"))
                .andExpect(jsonPath("$.updateURL").value("http://www.bing.com"))
                .andExpect(jsonPath("$.forceUpdate").value(true))
                .andExpect(jsonPath("$.latestVersion").value(2.18));
    }
}
