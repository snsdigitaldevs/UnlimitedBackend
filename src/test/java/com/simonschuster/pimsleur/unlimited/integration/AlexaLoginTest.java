package com.simonschuster.pimsleur.unlimited.integration;

import com.simonschuster.pimsleur.unlimited.services.login.LoginService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore
public class AlexaLoginTest {
    @Autowired
    LoginService loginService;

    @Test
    public void shouldGetSubFromAuth0Correctly() throws Exception {
        String sub = loginService.getAuthorizationSub("9-big-lauguages-pcm-courses@thoughtworks.com", "Pims9999!");

        assertNotNull(sub);
        assertEquals("auth0|5b18e3133e7e606465d37a0c", sub);
    }
}
