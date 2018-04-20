package com.simonschuster.pimsleur.unlimited.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

public class EDTRequestUtil {
    public static <T> T postToEdt(HttpEntity<String> entity, String url, Class<T> responseType) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(asList(TEXT_HTML, APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(converter);

        return restTemplate.postForObject(url, entity, responseType);
    }
}
