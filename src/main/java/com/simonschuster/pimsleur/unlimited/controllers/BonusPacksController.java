package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks.BonusPackInUnit;
import com.simonschuster.pimsleur.unlimited.services.bonusPacks.BonusPacksService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class BonusPacksController {

    @Autowired
    private BonusPacksService bonusPacksService;

    @ApiOperation(value = "PU bonusPacks of one course",
            notes = "PU courses reward bonus pack after each 5 lessons be finished.")
    @RequestMapping(value = "/puProduct/{productCode}/bonusPacksInfo", method = RequestMethod.GET)
    public List<BonusPackInUnit> getBonusPacksInUnit(@PathVariable("productCode") String productCode,
                  @RequestParam(value = "storeDomain", required = false) String storeDomain) throws IOException {
        return bonusPacksService.getBonusPacksInUnit(productCode, storeDomain);
    }
}
