package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.FreeLessonDto;
import com.simonschuster.pimsleur.unlimited.services.pcmFreeLessons.PcmFreeLessonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FreeLessonsController {
    @Autowired
    private PcmFreeLessonsService pcmFreeLessonsService;

    @RequestMapping(value = "/freeLessons", method = RequestMethod.GET)
    public List<FreeLessonDto> getFreeLessons() {
        return pcmFreeLessonsService.getPcmFreeLessons();
    }
}
