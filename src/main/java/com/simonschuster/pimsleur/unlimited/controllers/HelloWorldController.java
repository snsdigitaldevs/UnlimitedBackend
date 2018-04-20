package com.simonschuster.pimsleur.unlimited.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
//    @Autowired
//    private AddressBookRepository addressBookRepository;
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public AddressBook getUser(@PathVariable("id") int addressBookId) {
//        return addressBookRepository.getOne(addressBookId);
//    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
