package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.domain.AddressBook;
import com.simonschuster.pimsleur.unlimited.repo.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @Autowired
    private AddressBookRepository addressBookRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AddressBook getUser(@PathVariable("id") int addressBookId) {
        return addressBookRepository.getOne(addressBookId);
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
