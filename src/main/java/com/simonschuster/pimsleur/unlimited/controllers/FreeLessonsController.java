package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.LearnerInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.LearnerInfoService;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.PcmFreeLessonsService;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.PuFreeLessonsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FreeLessonsController {
    @Autowired
    private PcmFreeLessonsService pcmFreeLessonsService;
    
    @Autowired
    private PuFreeLessonsService puFreeLessonsService;
    
    @Autowired
    private LearnerInfoService learnerInfoService;
    
    @ApiOperation(value = "Get a list of all free lessons of both PU and PCM")
    @GetMapping(value = "/freeLessons")
    public List<AvailableProductDto> getFreeLessons(@RequestParam(value = "storeDomain") String storeDomain) {
        List<AvailableProductDto> pcmFreeLessons = pcmFreeLessonsService.getPcmFreeLessons(storeDomain);
        final List<AvailableProductDto> availableProducts = puFreeLessonsService
            .mergePuWithPcmFreeLessons(pcmFreeLessons);
        return storeDomain.equals("alexa") ? changeLanguageNameForAlexa(availableProducts) : availableProducts;
    }
    
    @ApiOperation(value = "Before trying free lessons, users are asked to fill in their emails. " +
        "Call this api to tell EDT the user's email and further actions will be taken by EDT.")
    @PostMapping(value = "/freeLessons/learnerInfo")
    public void learnerInfo(@RequestBody LearnerInfoBodyDTO learnerInfoBodyDTO) {
        learnerInfoService.LearnerInfo(learnerInfoBodyDTO);
    }
    
    private List<AvailableProductDto> changeLanguageNameForAlexa(List<AvailableProductDto> availableProducts) {
        availableProducts.forEach(product -> {
            if (product.getProductCode().equals("9781508277293")) {
                product.setLanguageName("Portuguese (European)");
            }
            if (product.getProductCode().equals("9781797129396")) {
                product.setLanguageName("Castilian Spanish");
            }
        });
        return availableProducts;
    }
}
