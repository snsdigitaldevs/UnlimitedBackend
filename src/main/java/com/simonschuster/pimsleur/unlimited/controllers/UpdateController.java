package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.update.CheckUpdateDto;
import com.simonschuster.pimsleur.unlimited.services.update.CheckUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
public class UpdateController {

    @Autowired
    private CheckUpdateService checkUpdateService;

    @GetMapping("/checkUpdate")
    public CheckUpdateDto checkUpdate(@RequestParam("version") Double version) {
        if (version == null) {
            throw new IllegalArgumentException("Version is not null");
        }
        return checkUpdateService.checkUpdate(version);
    }
}
