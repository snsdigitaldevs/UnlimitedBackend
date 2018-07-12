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
    @RequestMapping(value = "/freeLessons", method = RequestMethod.GET)
    public List<AvailableProductDto> getFreeLessons(@RequestParam(value = "storeDomain") String storeDomain) {
        return puFreeLessonsService
                .mergePuWithPcmFreeLessons(pcmFreeLessonsService.getPcmFreeLessons(storeDomain));
    }

    @ApiOperation(value = "Before trying free lessons, users are asked to fill in their emails. " +
            "Call this api to tell EDT the user's email and further actions will be taken by EDT.")
    @RequestMapping(value = "/freeLessons/learnerInfo", method = RequestMethod.POST)
    public void learnerInfo(@RequestBody LearnerInfoBodyDTO learnerInfoBodyDTO) {
        learnerInfoService.LearnerInfo(learnerInfoBodyDTO);
    }
}
