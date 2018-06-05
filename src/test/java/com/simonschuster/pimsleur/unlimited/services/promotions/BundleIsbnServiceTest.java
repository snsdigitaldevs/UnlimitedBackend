package com.simonschuster.pimsleur.unlimited.services.promotions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BundleIsbnServiceTest {

    @Autowired
    private BundleIsbnService bundleIsbnService;

    @Test
    public void shouldFindBundleIsbnsThatIncludeAChildIsbn() {
        // isbn for chinese level 1
        List<String> bundleIsbns = bundleIsbnService.getBundleIsbnsOf("9781508231431");

        //bundles should be chinese 1-2, 1-4 and 1-5
        assertThat(bundleIsbns, containsInAnyOrder("9781508260257", "9781508231424", "9781508227939"));
    }
}