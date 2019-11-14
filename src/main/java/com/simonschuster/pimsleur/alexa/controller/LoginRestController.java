package com.simonschuster.pimsleur.alexa.controller;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.constants.StoreDomainConstants;
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

import static com.simonschuster.pimsleur.unlimited.services.customer.SignUpService.EMAIL_ALREADY_REGISTERED_ERROR_MESSAGE;
import static com.simonschuster.pimsleur.unlimited.services.customer.SignUpService.PASSWORD_INVALID_MESSAGE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class LoginRestController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private SignUpService signUpService;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = POST)
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
            noticeEDTForNewUserInAuth0(email, password);

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

    private void noticeEDTForNewUserInAuth0(@RequestParam(name = "email") String email,
                                            @RequestParam(name = "password") String password) {
        SignUpBodyDTO signUpBodyDto = new SignUpBodyDTO();
        signUpBodyDto.setEmail(email);
        signUpBodyDto.setPassword(password);
        signUpBodyDto.setStoreDomain(StoreDomainConstants.ALEXA_STORE_DOMAIN);
        try {
            signUpService.signUp(signUpBodyDto);
        } catch (ParamInvalidException e) {
            //if error message is caused by sign up -3011 or -1 error, then we ignore it
            if (!e.getMessage().equals(EMAIL_ALREADY_REGISTERED_ERROR_MESSAGE)
                    && !e.getMessage().equals(PASSWORD_INVALID_MESSAGE)) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}
