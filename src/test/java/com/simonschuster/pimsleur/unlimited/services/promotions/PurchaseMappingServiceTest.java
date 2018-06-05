package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.simonschuster.pimsleur.unlimited.data.dto.promotions.PurchaseMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PurchaseMappingServiceTest {

    @Autowired
    private PurchaseMappingService purchaseMappingService;

    @Test
    public void shouldGetPurchaseMappingForIsbn() {
        // isbn match
        PurchaseMapping purchaseMapping = purchaseMappingService.getPurchaseMappingFor("9781508231431");
        assertThat(purchaseMapping.getISBN(), is("9781508231431"));

        // other format one match
        purchaseMapping = purchaseMappingService.getPurchaseMappingFor("9781508275589");
        assertThat(purchaseMapping.getISBN(), is("9781442394896"));

        // other format two match
        purchaseMapping = purchaseMappingService.getPurchaseMappingFor("9781508275688");
        assertThat(purchaseMapping.getISBN(), is("9781442394896"));

        // other format three match
        purchaseMapping = purchaseMappingService.getPurchaseMappingFor("9781442383265");
        assertThat(purchaseMapping.getISBN(), is("9781508231424"));

        // no match
        purchaseMapping = purchaseMappingService.getPurchaseMappingFor("123");
        assertThat(purchaseMapping, is(nullValue()));
    }
}