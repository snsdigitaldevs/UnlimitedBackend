package com.simonschuster.pimsleur.alexa.controller;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp.SignUpBodyDTO;
import com.simonschuster.pimsleur.unlimited.services.customer.SignUpService;
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
    @Autowired
    private SignUpService signUpService;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/login")
    public String loginSubmission(HttpServletRequest request,
                                  @RequestParam(name = "email") String email,
                                  @RequestParam(name = "password") String password,
                                  @RequestParam(name = "state") String state,
                                  @RequestParam(name = "redirect_uri") String redirectUri,
                                  Model model) {
        String responseRedirectUrl = "";
        try {
            String authorizationSub = loginService.getAuthorizationSub(email, password);
            //Generate necessary data in EDT backend for user who signed up on auth0
            noticeEDTForNewUserInAuth0(email);

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

    private void noticeEDTForNewUserInAuth0(@RequestParam(name = "email") String email) {
        SignUpBodyDTO signUpBodyDto = new SignUpBodyDTO();
        signUpBodyDto.setEmail(email);
        //Hardcode storedomain as android_inapp because EDT only accept "android_inapp" or "ios_inapp" in sign up API.
        signUpBodyDto.setStoreDomain("android_inapp");
        try {
            signUpService.signUp(signUpBodyDto);
        } catch (ParamInvalidException e) {
            if (!e.getMessage().equals(signUpService.EMAIL_ALREADY_REGISTERED_ERROR_MESSAGE)) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}
