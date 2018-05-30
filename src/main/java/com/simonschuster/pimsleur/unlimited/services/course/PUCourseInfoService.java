package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.PuProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

@Service
public class PUCourseInfoService {
    public static final int MP3_MEDIA_TYPE = 1;
    public static final String KEY_DOWNLOAD = "Download";

    @Autowired
    private ApplicationConfiguration config;

    private static final Logger logger = LoggerFactory.getLogger(PUCourseInfoService.class);

    /**
     * Get product info for Pimsleur Unlimited.
     *
     * @param productCode
     */
    public AggregatedProductInfo getPuProductInfo(String productCode) {
        try {
            AggregatedProductInfo productInfo = new AggregatedProductInfo();
            productInfo.setPuProductInfo(getProductInfoFromPu(productCode));
            return productInfo;
        } catch (Exception exception) {
            logger.error("Exception occured when get product info with PU product code.");
            exception.printStackTrace();
            throw new PimsleurException("Exception occured when get product info with PU product code.");
        }
    }

    /**
     * Send reqeust to get Pimsleur Unlimited product info.
     *
     * @param productCode
     * @return
     */
    private PuProductInfo getProductInfoFromPu(String productCode) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(asList(TEXT_HTML, APPLICATION_JSON));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> requestEntity = new HttpEntity<>(
                String.format(config.getApiParameter("unlimitedProductInfoDefaultParameters"), productCode),
                headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(converter);

        String productInfoApiUrl = config.getProperty("edt.api.productInfoApiUrl");
        return restTemplate.postForObject(productInfoApiUrl, requestEntity, PuProductInfo.class);
    }

}
