package com.simonschuster.pimsleur.unlimited.services.update;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckUpdateServiceTest extends TestCase {

    @Autowired
    private CheckUpdateService checkUpdateService;


    @Test
    public void should_return_true_where_2_16_2_18() {
        boolean result = checkUpdateService.compareVersion("2.16", "2.18");
        assertTrue(result);
    }

    @Test
    public void should_return_false_where_2_16_2_15() {
        boolean result = checkUpdateService.compareVersion("2.16", "2.15");
        assertFalse(result);
    }

    @Test
    public void should_return_false_where_2_15_2_15() {
        boolean result = checkUpdateService.compareVersion("2.15", "2.15");
        assertFalse(result);
    }

    @Test
    public void should_return_false_where_2_15_1_2_15() {
        boolean result = checkUpdateService.compareVersion("2.15.1", "2.15");
        assertFalse(result);
    }

    @Test
    public void should_return_true_where_2_8_2_15() {
        boolean result = checkUpdateService.compareVersion("2.8", "2.15");
        assertTrue(result);
    }

    @Test
    public void should_return_false_where_2_15_2_15_1() {
        boolean result = checkUpdateService.compareVersion("2.15", "2.15.1");
        assertFalse(result);
    }

    @Test
    public void should_return_true_where_2_15_1_2_16() {
        boolean result = checkUpdateService.compareVersion("2.15.1", "2.16");
        assertTrue(result);
    }

}