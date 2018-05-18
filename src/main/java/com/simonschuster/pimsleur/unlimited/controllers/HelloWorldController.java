package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ApplicationConfiguration configuration;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/hello", method = GET)
    public String hello() {
        return "from properties file " + configuration.getProperty("edt.api.customerInfoApiUrl");
    }
}
