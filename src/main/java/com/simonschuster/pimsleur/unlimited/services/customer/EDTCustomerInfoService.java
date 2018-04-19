package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.domain.customer.AggregatedCustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.domain.customer.CustomerInfo;
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
public class EDTCustomerInfoService {

    @Autowired
    private ApplicationConfiguration config;

    public AggregatedCustomerInfo getCustomerInfos(String sub) {
        return new AggregatedCustomerInfo(
                getCourse(sub,
                        config.getApiParameter("unlimitedCustomerAction"),
                        config.getApiParameter("unlimitedDomain")),
                getCourse(sub,
                        config.getApiParameter("pcmCustomerAction"),
                        config.getApiParameter("pcmDomain"))
        );
    }

    private CustomerInfo getCourse(String sub, String action, String domain) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(asList(TEXT_HTML, APPLICATION_JSON));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(
                String.format(config.getApiParameter("customerInfoDefaultParameters"), sub, action, domain),
                headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(converter);

        return restTemplate.postForObject(config.getProperty("edt.api.customerInfoApiUrl"), entity, CustomerInfo.class);
    }
}
