package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.usage.MediaItemUsageBody;
import com.simonschuster.pimsleur.unlimited.services.usage.MediaItemUsageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class MediaItemUsageController {

    @Autowired
    private MediaItemUsageService mediaItemUsageService;

    @ApiOperation(value = "Media item usage",
            notes = "Tell EDT a user has used a media item. So that customer service knows if this user can get a refund.")
    @RequestMapping(value = "/account/{customerId}/mediaItem/{mediaItemId}/usage", method = POST)
    public void reportUsage(
            @PathVariable("customerId") String customerId,
            @PathVariable("mediaItemId") String mediaItemId,
            @RequestBody MediaItemUsageBody mediaItemUsageBody) {
        mediaItemUsageService.reportMediaItemUsage(customerId, mediaItemId, mediaItemUsageBody.getIdentityVerificationToken());
    }
}
