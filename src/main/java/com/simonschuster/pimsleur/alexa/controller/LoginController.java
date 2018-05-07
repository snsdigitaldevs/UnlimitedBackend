package com.simonschuster.pimsleur.alexa.controller;

import com.simonschuster.pimsleur.unlimited.data.auth0.Auth0TokenInfo;
import com.simonschuster.pimsleur.unlimited.services.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    public static final Map<String, String> URI_VARIABLES_FOR_LOGIN = new HashMap<>();
    private static String AUTH0_DOMAIN = "mg2-ss-prod.auth0.com";
    private static String AUTH0_CLIENT_ID = "OYONqZ4zLlVruKMT9FhCtlV1idp7wrPJ";

    @GetMapping("/login")
//  http://localhost:8080/login?state=abc&client_id=alexa-skill&scope=order_car%20basic_profile&response_type=code&redirect_uri=https%3A%2F%2Fpitangui.amazon.com%2Fspa%2Fskill%2Faccount-linking-status.html%3FvendorId%3DAAAAAAAAAAAAAA
    public String login(HttpServletRequest request, @RequestParam(name="state") String state,
                        @RequestParam(name="client_id") String clientId,
                        @RequestParam(name="response_type") String responseType,
                        @RequestParam(name="scope") String scope,
                        @RequestParam(name="redirect_uri") String redirectUri,
                        Model model) {
        request.getSession().setAttribute("state", state);
        request.getSession().setAttribute("redirect_uri", redirectUri);

        return "login.html";
    }

    @GetMapping("/loginSubmit")
    public String loginSubmission(HttpServletRequest request,
                                  @RequestParam(name="email") String userName,
                                  @RequestParam(name="password") String password,
                                  Model model) {
        Auth0TokenInfo auth0TokenInfo = loginService.getAuthorizationInfoFromAuth0(userName, password);

        String state = (String) request.getSession().getAttribute("state");
        String redirectUri = (String) request.getSession().getAttribute("redirect_uri");

        String tokenType = auth0TokenInfo.getToken_type();
        String accessToken = auth0TokenInfo.getAccess_token();

        String redirectFullUrl = new StringBuilder().append(redirectUri).append("#")
                .append("state=").append(state).append("&")
                .append("access_token=").append(accessToken).append("&")
                .append("token_type").append(tokenType)
                .toString();

        return "redirect:" + redirectFullUrl;
    }
}
