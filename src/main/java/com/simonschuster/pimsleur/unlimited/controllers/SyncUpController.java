package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.syncUp.SyncUpDto;
import com.simonschuster.pimsleur.unlimited.services.syncState.SyncUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class SyncUpController {

    @Autowired
    private SyncUpService syncUpService;

    @RequestMapping(
            value = "/account/{customerId}/subUser/{subUserId}" +
                    "/product/{productCode}/mediaItem/{mediaItemId}/progress",
            method = POST,
            consumes = "application/json")
    public Long puSyncUp(@PathVariable("customerId") String customerId,
                         @PathVariable("subUserId") String subUserId,
                         @PathVariable("productCode") String productCode,
                         @PathVariable("mediaItemId") String mediaItemId,
                         @RequestBody SyncUpDto syncUpDto) throws Exception {

        return syncUpService.syncUpPUProgress(customerId, subUserId, productCode, mediaItemId, syncUpDto);
    }


    @RequestMapping(
            value = "/account/{customerId}/product/{productCode}/mediaSet/{mediaSetID}/mediaItem/{mediaItemId}/progress",
            method = POST,
            consumes = "application/json")
    public Long pcmSyncUP(@PathVariable("customerId") String customerId,
                          @PathVariable("productCode") String productCode,
                          @PathVariable("mediaSetID") String mediaSetID,
                          @PathVariable("mediaItemId") String mediaItemId,
                          @RequestBody SyncUpDto syncUpDto) throws Exception {
        return syncUpService.syncUpPcmProgress(customerId, productCode, mediaSetID, mediaItemId, syncUpDto);
    }
}
