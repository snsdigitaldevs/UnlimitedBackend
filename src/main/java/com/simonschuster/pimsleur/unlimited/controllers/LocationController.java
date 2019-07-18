package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.price.LocationInfoDTO;
import com.simonschuster.pimsleur.unlimited.services.promotions.LocationMappingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LocationController {

    @Autowired
    private LocationMappingService locationMappingService;

    @ApiOperation(value = "Get location from IP address")
    @GetMapping("/location")
    public LocationInfoDTO getUpsellInfo(HttpServletRequest request) {
        return locationMappingService.getLocation(request);
    }
}
