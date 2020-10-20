package com.simonschuster.pimsleur.unlimited.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HelloWorldController {
    //    @Autowired
    //    private AddressBookRepository addressBookRepository;
    //
    //    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    //    public AddressBook getUser(@PathVariable("id") int addressBookId) {
    //        return addressBookRepository.getOne(addressBookId);
    //    }

    @ApiOperation(value = "This api is called after a new deployment to verify if the new version is up and running")
    @RequestMapping(value = "/", method = GET)
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
