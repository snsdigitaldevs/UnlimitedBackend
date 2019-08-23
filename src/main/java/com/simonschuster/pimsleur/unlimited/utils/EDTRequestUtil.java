package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.aop.LogCostTimeAspect;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

public class EDTRequestUtil {

  private static final Logger LOG = LoggerFactory.getLogger(LogCostTimeAspect.class);
  private static final RestTemplate REST_TEMPLATE;

  static {
    REST_TEMPLATE = new RestTemplate();
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(asList(TEXT_HTML, APPLICATION_JSON));
    REST_TEMPLATE.getMessageConverters().add(converter);
  }

  public static <T> T postToEdt(HttpEntity<String> httpEntity, String url, Class<T> responseType) {
    long startTime = System.currentTimeMillis();
    T response = REST_TEMPLATE.postForObject(url, httpEntity, responseType);
    LOG.info("Request[{}] for EDT cost {}ms", url, System.currentTimeMillis() - startTime);
    checkResult(url, httpEntity, response);
    return response;
  }

  public static <T> T getFromEdt(String url, Class<T> responseType, HttpEntity<String> httpEntity) {
    long startTime = System.currentTimeMillis();
    T response = REST_TEMPLATE.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    LOG.info("Request[{}] for EDT {} cost {}ms", url, System.currentTimeMillis() - startTime);
    checkResult(url, httpEntity, response);
    return response;
  }

  private static <T> void checkResult(String url, HttpEntity<String> httpEntity, T response) {
    if (response instanceof EdtResponseCode) {
      int resultCode = ((EdtResponseCode) response).getResultCode();
      if (EdtResponseCode.RESULT_OK != resultCode) {
        LOG.error("Request:[{}] execute error, params is {} ,response code is {}", url,
            httpEntity.toString(), resultCode);
      }
    }
  }
}
