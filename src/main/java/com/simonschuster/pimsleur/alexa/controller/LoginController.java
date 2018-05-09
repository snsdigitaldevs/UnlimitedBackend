package com.simonschuster.pimsleur.alexa.controller;

import com.simonschuster.pimsleur.unlimited.data.auth0.Auth0TokenInfo;
import com.simonschuster.pimsleur.unlimited.services.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    public static final Map<String, String> URI_VARIABLES_FOR_LOGIN = new HashMap<>();

    @GetMapping("/login")
//  http://localhost:8080/login?state=abc&client_id=alexa-skill&scope=order_car%20basic_profile&response_type=code&redirect_uri=https%3A%2F%2Fpitangui.amazon.com%2Fspa%2Fskill%2Faccount-linking-status.html%3FvendorId%3DAAAAAAAAAAAAAA
    public String login(HttpServletRequest request, @RequestParam(name="state") String state,
                        @RequestParam(name="client_id") String clientId,
                        @RequestParam(name="response_type") String responseType,
                        @RequestParam(name="scope") String scope,
                        @RequestParam(name="redirect_uri") String redirectUri,
                        Model model) {
        model.addAttribute("state", state);
        model.addAttribute("redirect_uri", redirectUri);

        return "login";
    }

    @PostMapping("/login")
    public String loginSubmission(HttpServletRequest request,
                                  @RequestParam(name="email") String userName,
                                  @RequestParam(name="password") String password,
                                  @RequestParam(name="state") String state,
                                  @RequestParam(name="redirect_uri") String redirectUri,
                                  Model model) {
        String authorizationSub = loginService.getAuthorizationSub(userName, password);

        String redirectFullUrl = new StringBuilder().append(redirectUri).append("#")
                .append("state=").append(state).append("&")
                .append("access_token=").append(authorizationSub).append("&")
                //Alexa document said token_type must be "Bear" which align with what we got from EDT API.
                //token_type won't be used on alexa endpoint, it's included in access_token's value sub.
                .append("token_type=").append("Bear")
                .toString();

        return "redirect:" + redirectFullUrl;
    }
}
