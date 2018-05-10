package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.FreeLessonDto;
import com.simonschuster.pimsleur.unlimited.services.pcmFreeLessons.PcmFreeLessonsService;
import com.simonschuster.pimsleur.unlimited.services.pcmFreeLessons.PuFreeLessonsService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/freeLessons", method = RequestMethod.GET)
    public List<FreeLessonDto> getFreeLessons() {
        return puFreeLessonsService
                .mergePuWithPcmFreeLessons(pcmFreeLessonsService.getPcmFreeLessons());
    }
}
