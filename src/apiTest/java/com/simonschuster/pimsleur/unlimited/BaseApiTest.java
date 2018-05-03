package com.simonschuster.pimsleur.unlimited;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


/**
 * Created by yteng on 2/6/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
})
public abstract class BaseApiTest {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected WebApplicationContext context;

    protected String resourceToString(Resource resource) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    protected MockMvcRequestSpecification given() {
        return RestAssuredMockMvc.given().webAppContextSetup(context);
    }

}
