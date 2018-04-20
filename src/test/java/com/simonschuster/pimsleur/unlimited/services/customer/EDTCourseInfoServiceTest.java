package com.simonschuster.pimsleur.unlimited.services.customer;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.ProductInformation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class EDTCourseInfoServiceTest {
    @Autowired
    private EDTCourseInfoService edtCourseInfoService;

    @Test
    public void shouldGetCorrectResponseFromEDTService() throws Exception {
        //mock edt api response
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh"),
                eq(form("gccfs"), "[\"9781508243328\"]")
                ))
                .response(file("src/test/resources/edtProductInfoResponse.json"));

        running(server, new Runnable() {
            @Override
            public void run() {
                ProductInformation productInfo = edtCourseInfoService.getCourseInfos("9781508243328");
                assertThat(productInfo.getResultCode(),is(1));
                assertNotNull(productInfo.getResultData().getCourseConfigs());
                assertNotNull(productInfo.getResultData().getMediaSets());
            }
        });
    }
}