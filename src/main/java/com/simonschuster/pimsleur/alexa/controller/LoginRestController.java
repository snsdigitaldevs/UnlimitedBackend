package com.simonschuster.pimsleur.alexa.controller;

import com.simonschuster.pimsleur.unlimited.services.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginRestController {

    @Autowired
    private LoginService loginService;
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/login")
    public String loginSubmission(HttpServletRequest request,
                                  @RequestParam(name = "email") String userName,
                                  @RequestParam(name = "password") String password,
                                  @RequestParam(name = "state") String state,
                                  @RequestParam(name = "redirect_uri") String redirectUri,
                                  Model model) {
        String responseRedirectUrl = "";
        try {
            String authorizationSub = loginService.getAuthorizationSub(userName, password);
            responseRedirectUrl = new StringBuilder().append(redirectUri).append("#")
                    .append("state=").append(state).append("&")
                    .append("access_token=").append(authorizationSub).append("&")
                    //Alexa document said token_type must be "Bear" which align with what we got from EDT API.
                    //token_type won't be used on alexa endpoint, it's included in access_token's value sub.
                    .append("token_type=").append("Bear")
                    .toString();
        } catch (Exception e) {
            responseRedirectUrl = "/login?loginStatus=fail";
            logger.error("Error when submit login on alexa.");
        }

        return responseRedirectUrl;
    }
}
