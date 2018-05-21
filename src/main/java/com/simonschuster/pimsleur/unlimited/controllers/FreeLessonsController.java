package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.FreeLessonDto;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.LearnerInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.LearnerInfoService;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.PcmFreeLessonsService;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.PuFreeLessonsService;
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

    @RequestMapping(value = "/freeLessons", method = RequestMethod.GET)
    public List<FreeLessonDto> getFreeLessons() {
        return puFreeLessonsService
                .mergePuWithPcmFreeLessons(pcmFreeLessonsService.getPcmFreeLessons());
    }

    @RequestMapping(value = "/freeLessons/learnerInfo", method = RequestMethod.POST)
    public void learnerInfo(@RequestBody LearnerInfoBodyDTO learnerInfoBodyDTO) {
        learnerInfoService.LearnerInfo(learnerInfoBodyDTO);
    }
}
