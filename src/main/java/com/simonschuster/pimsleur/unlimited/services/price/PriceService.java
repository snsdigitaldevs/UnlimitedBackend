package com.simonschuster.pimsleur.unlimited.services.price;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.auth0.Auth0TokenInfo;
import com.simonschuster.pimsleur.unlimited.data.dto.price.DemandwareShopInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.getFromEdt;
import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class PriceService {
    private static final Logger logger = LoggerFactory.getLogger(PriceService.class);

    @Autowired
    private ApplicationConfiguration config;

    public DemandwareShopInfo getDemandwareShopInfo(String productCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-dw-client-id", config.getProperty("demandware.api.getPrice.clientId"));
        try {
            DemandwareShopInfo info = getFromEdt(
                    String.format(config.getProperty("demandware.api.getPrice.url"), productCode),
                    DemandwareShopInfo.class, new HttpEntity<>(headers));
            return info;
        } catch (Exception e) {
            logger.error("Error when request price from demandware.");
        }
        return new DemandwareShopInfo();
    }

    private String getDemandwareToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Auth0TokenInfo auth0TokenInfo = postToEdt(
                new HttpEntity<>(
                        String.format(config.getProperty("demandware.api.parameters"),
                                config.getAuth0ApiParameter("demandware.api.clientId"),
                                config.getAuth0ApiParameter("demandware.api.clientSecret")),
                        headers),
                    config.getProperty("demandware.api.url"),
                Auth0TokenInfo.class);
        return auth0TokenInfo.getAccess_token();
    }

}
