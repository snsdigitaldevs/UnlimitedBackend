package com.simonschuster.pimsleur.alexa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private Environment env;


    @GetMapping("/login")
//  http://localhost:8080/login?state=abc&client_id=alexa-skill&scope=order_car%20basic_profile&response_type=code&redirect_uri=https%3A%2F%2Fpitangui.amazon.com%2Fspa%2Fskill%2Faccount-linking-status.html%3FvendorId%3DAAAAAAAAAAAAAA
    public String login(HttpServletRequest request,
                        @RequestParam(name = "state", required = false) String state,
                        @RequestParam(name = "client_id", required = false) String clientId,
                        @RequestParam(name = "response_type", required = false) String responseType,
                        @RequestParam(name = "scope", required = false) String scope,
                        @RequestParam(name = "redirect_uri", required = false) String redirectUri,
                        @RequestParam(name = "loginStatus", required = false) String loginStatus,
                        Model model) {
        model.addAttribute("state", state);
        model.addAttribute("redirect_uri", redirectUri);
        model.addAttribute("auth0_domain", env.getProperty("alexa.auth0.domain"));
        model.addAttribute("auth0_client_id", env.getProperty("alexa.auth0.client.id"));

        return "login";
    }
}
