package com.simonschuster.pimsleur.unlimited.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    
    @ApiOperation(value = "This api is called after a new deployment to verify if the new version is up and running")
    @GetMapping(value = "/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
