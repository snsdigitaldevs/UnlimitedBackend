package com.simonschuster.pimsleur.unlimited.services.price;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.auth0.Auth0TokenInfo;
import com.simonschuster.pimsleur.unlimited.data.dto.price.DemandwareShopInfo;
import com.simonschuster.pimsleur.unlimited.data.dto.price.MG2Offer;
import com.simonschuster.pimsleur.unlimited.data.dto.price.MG2ShopInfo;
import com.simonschuster.pimsleur.unlimited.data.dto.price.PriceInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.getFromEdt;
import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class PriceService {
    private static final Logger logger = LoggerFactory.getLogger(PriceService.class);

    @Autowired
    private ApplicationConfiguration config;

    public PriceInfoDTO getDemandwareShopInfo(String productCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-dw-client-id", config.getProperty("demandware.api.getPrice.clientId"));
        try {
            DemandwareShopInfo info = getFromEdt(
                    String.format(config.getProperty("demandware.api.getPrice.url"), productCode),
                    DemandwareShopInfo.class,
                    new HttpEntity<>(headers));
            return new PriceInfoDTO(info.getPrice(), info.getCurrency(), info.getName(), productCode);
        } catch (Exception e) {
            logger.error("Error when request price from Demandware.");
        }
        return new PriceInfoDTO();
    }

    public PriceInfoDTO getMG2ShopInfo(String pid) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-MediaGroupCode", "Simon");
        headers.set("X-ClientCode", "N/A");
        headers.set("X-PaperCode", "N/A");
        headers.set("X-SourceSystem", "PimsleurDesktopApp");
        headers.set("Authorization", config.getProperty("mg2.api.getPrice.authorization"));

        MG2ShopInfo info = getFromEdt(
                String.format(config.getProperty("mg2.api.getPrice.url"), pid),
                MG2ShopInfo.class,
                new HttpEntity<>(headers));

        if (!CollectionUtils.isEmpty(info.getErrors())) {
            logger.error("Error when request price from MG2", info.getErrors().get(0));
            return new PriceInfoDTO();
        }

        if (Objects.nonNull(info.getResult())) {
            MG2Offer offer = info.getResult().getOffers().get(0);
            Float price = offer.getDryRunAmount();
            String currency = offer.getProducts().get(0).getCurrency();
            return new PriceInfoDTO(price, currency, offer.getName(), offer.getSubscriptionLevel(), pid);
        } else {
            return new PriceInfoDTO();
        }
    }

}
