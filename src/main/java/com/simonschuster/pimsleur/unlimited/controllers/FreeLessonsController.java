package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.FreeLessonDto;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.LearnerInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.LearnerInfoService;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.PcmFreeLessonsService;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.PuFreeLessonsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public List<FreeLessonDto> getFreeLessons() {
        return puFreeLessonsService
                .mergePuWithPcmFreeLessons(pcmFreeLessonsService.getPcmFreeLessons());
    }

    @ApiOperation(value = "Tell us your email so that we can spam you")
    @RequestMapping(value = "/freeLessons/learnerInfo", method = RequestMethod.POST)
    public void learnerInfo(@RequestBody LearnerInfoBodyDTO learnerInfoBodyDTO) {
        learnerInfoService.LearnerInfo(learnerInfoBodyDTO);
    }
}
