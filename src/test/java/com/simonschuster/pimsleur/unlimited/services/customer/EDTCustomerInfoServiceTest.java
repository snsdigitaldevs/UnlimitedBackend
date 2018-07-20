package com.simonschuster.pimsleur.unlimited.services.customer;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.CustomerInfoDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.AggregatedCustomerInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class EDTCustomerInfoServiceTest {

    @Autowired
    private EDTCustomerInfoService edtCustomerInfoService;

    @Test
    public void shouldGetUnlimitedAndPCMCustomerInfos() throws Exception {

        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rsovkolfqxrjl.php")),
                eq(form("action"), "pu_blmqide")))
                .response(file("src/test/resources/unlimitedCustInfoResponse.json"));
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rsovkolfqxrjl.php")),
                eq(form("action"), "pcm_blmqide")))
                .response(file("src/test/resources/pcmCustInfoResponse.json"));

        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/uxpzs.php")),
                eq(form("store_domain"), "ss_pu")))
                .response(file("src/test/resources/unlimitedSyncStateResponse.json"));
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/uxpzs.php")),
                eq(form("store_domain"), "")))
                .response(file("src/test/resources/pcmSyncStateResponse.json"));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                AggregatedCustomerInfo customerInfos =
                        edtCustomerInfoService.getCustomerInfos("whatever", "", "email");

                assertThat(customerInfos.getPcmCustomerInfo().getResultCode(), is(1));
                assertThat(customerInfos.getUnlimitedCustomerInfo().getResultCode(), is(1));

                CustomerInfoDTO customerInfoDTO = customerInfos.toDto();
                List<ProgressDTO> currentProgresses = customerInfoDTO.getProgresses().stream()
                        .filter(prog -> prog.getCurrent())
                        .collect(toList());
                assertThat(currentProgresses.size(), is(3));
            }
        });
    }
}
