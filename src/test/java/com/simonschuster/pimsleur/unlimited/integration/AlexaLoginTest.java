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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore
public class AlexaLoginTest {
    @Autowired
    LoginService loginService;

    @Test
    public void shouldGetSubFromAuth0Correctly() throws Exception {
        String sub = loginService.getAuthorizationSub("sean0320c@mailinator.com", "Pims9999");

        assertEquals("auth0|5ab1728e1d2fb71e499dde01", sub);
    }
}
