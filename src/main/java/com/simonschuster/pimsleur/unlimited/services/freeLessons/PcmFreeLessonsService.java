package com.simonschuster.pimsleur.unlimited.services.freeLessons;

import com.simonschuster.pimsleur.unlimited.aop.annotation.LogCostTime;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import com.simonschuster.pimsleur.unlimited.data.edt.freeLessonsList.PCMProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.freeLessonsList.PCMProducts;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
public class PcmFreeLessonsService {

    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    @LogCostTime
    public List<AvailableProductDto> getPcmFreeLessons(String storeDomain) {
        PCMProducts pcmProducts =
                postToEdt(createPostBody(storeDomain), config.getProperty("edt.api.pcmProductsApiUrl"), PCMProducts.class);

        if (pcmProducts.getResultData() != null) {
            return pcmProducts.getResultData().getCourseLanguages()
                    .values().stream()
                    .flatMap(Collection::stream)
                    .filter(PCMProduct::isLevelOne)
                    .filter(PCMProduct::isNotSubscription)
                    .map(PCMProduct::pcmProductToDto)
                    .collect(toList());
        }
        return emptyList();
    }

    private HttpEntity<String> createPostBody(String storeDomain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(storeDomain);
        return new HttpEntity<>(format(config.getApiParameter("pcmProductsParameters"), appId), headers);
    }
}
