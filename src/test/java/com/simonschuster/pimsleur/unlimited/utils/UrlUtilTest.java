package com.simonschuster.pimsleur.unlimited.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class UrlUtilTest {
    @Test
    public void shouldEncodeUrlCorrectly() throws Exception {
        String puOriginUrl = "https://install.pimsleurunlimited.com/staging_n/common/mandarinchinese/Mandarin Chinese I/audio/9781442394872_Mandarin_Chinese1_U01_Lesson.mp3";

        String encodedUrl = UrlUtil.encodeUrl("https://install.pimsleurunlimited.com/staging_n/common", puOriginUrl);

        assertEquals("https://install.pimsleurunlimited.com/staging_n/common/mandarinchinese/Mandarin%20Chinese%20I/audio/9781442394872_Mandarin_Chinese1_U01_Lesson.mp3", encodedUrl);
    }
}