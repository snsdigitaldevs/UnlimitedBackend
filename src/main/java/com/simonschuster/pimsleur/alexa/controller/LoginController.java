package com.simonschuster.pimsleur.alexa.controller;

import com.simonschuster.pimsleur.unlimited.services.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    public static final Map<String, String> URI_VARIABLES_FOR_LOGIN = new HashMap<>();
    private static String AUTH0_DOMAIN = "mg2-ss-prod.auth0.com";
    private static String AUTH0_CLIENT_ID = "OYONqZ4zLlVruKMT9FhCtlV1idp7wrPJ";

    @GetMapping("/loginSubmit")
    public String loginSubmission(@RequestParam(name="email") String userName,
                                  @RequestParam(name="password") String password,
                                  Model model) {

        String sub = loginService.getAuthorizationSub(userName, password);
        //redirect is configured in amazon alexa portal
        return null;
    }
}
