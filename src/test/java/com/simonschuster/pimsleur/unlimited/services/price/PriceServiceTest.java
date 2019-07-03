package com.simonschuster.pimsleur.unlimited.services.price;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import com.simonschuster.pimsleur.unlimited.data.dto.price.PriceInfoDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.github.dreamhead.moco.Moco.and;
import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.eq;
import static com.github.dreamhead.moco.Moco.file;
import static com.github.dreamhead.moco.Moco.form;
import static com.github.dreamhead.moco.Moco.httpServer;
import static com.github.dreamhead.moco.Moco.uri;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class PriceServiceTest {
    private String PID = "200";
    private String PRODUCT_CODE = "9781442394902";

    @Autowired
    private PriceService priceService;

    @Test
    public void shouldGetPriceFromDemandware() throws Exception {
        HttpServer server = httpServer(12306);
        server.get(and(
                by(uri("/s/Pimsleur/dw/shop/v15_1/products/9781442394902/prices"))))
                .response(file("src/test/resources/demandwarePriceResponse.json"));
        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                PriceInfoDTO info = priceService.getDemandwareShopInfo(PRODUCT_CODE);
                assertThat(info.getCurrency(), is("USD"));
                assertThat(info.getName(), is("Pimsleur Chinese (Mandarin) Level 4 Premium"));
                assertThat(info.getPrice(), is(150f));
            }
        });
    }

    @Test
    public void shouldGetPriceFromMG2() throws Exception {
        HttpServer server = httpServer(12306);
        server.get(and(
                by(uri("/Promotions/200/Offers"))))
                .response(file("src/test/resources/MG2PriceResponse.json"));
        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                PriceInfoDTO info = priceService.getMG2ShopInfo(PID);
                assertThat(info.getCurrency(), is("USD"));
            }
        });
    }

}