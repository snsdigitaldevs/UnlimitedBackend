package com.simonschuster.pimsleur.unlimited.utils;

import org.junit.Test;

import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.PU_FREE_LESSONS;
import static org.junit.Assert.assertNotNull;

public class HardCodedProductsUtilTest {
    @Test
    public void shouldGetPuFreeLessonAndNotNull() {
        assertNotNull(PU_FREE_LESSONS);
    }
}