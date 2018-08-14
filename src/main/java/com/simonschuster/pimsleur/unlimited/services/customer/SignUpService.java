package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.alexa.StoreDomainUtil;
import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp.SignUpBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp.SignUpDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.SignUp.SignUpEDT;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class SignUpService {
    public static final String EMAIL_INVALID_ERROR_MESSAGE = "Please check email format or if it is already registered.";
    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private AppIdService appIdService;

    public SignUpDTO signUp(SignUpBodyDTO signUpBodyDTO) {
        String url = applicationConfiguration.getProperty("edt.api.signUp.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String userName = signUpBodyDTO.getUserName() == null ? "" : "&worf=" + signUpBodyDTO.getUserName();

        String appId = appIdService.getAppId(signUpBodyDTO.getStoreDomain());
        //EDT doesn't accept "alexa" as store_domain for this API, so set ss_pu for alexa only for this API.
        String storeDomain = signUpBodyDTO.getStoreDomain().equals(StoreDomainUtil.ALEXA_STORE_DOMAIN) ?
                "ss_pu" : signUpBodyDTO.getStoreDomain();

        String email = signUpBodyDTO.getEmail();
        String password = signUpBodyDTO.getPassword();
        String countryCode = signUpBodyDTO.getCountryCode();
        HttpEntity<String> entity = new HttpEntity<>(String.format(
                applicationConfiguration.getProperty("edt.api.signUp.parameters.signUp"),
                userName, password, appId,
                email, storeDomain, countryCode
        ), headers);
        SignUpEDT response = postToEdt(entity, url, SignUpEDT.class);

        if (!response.getResultCode().equals(1)) {
            String errorMessage;
            switch (response.getResultCode()) {
                case -3011:
                    errorMessage = EMAIL_INVALID_ERROR_MESSAGE;
                    break;
                case -1:
                    errorMessage = "Password must be at least 8 characters,  " +
                            "including: lower case letters,  upper case letters,  numbers,  and special characters.";
                    break;
//                     invalid email cause different error codes
//                     e.g.: xxx334.xxx.online cause -1
//                           xxx333.xxx.online cause -3011
//                           in some situations cause 0
/*
                case 0:
                    errorMessage = "Please input valid email address.";
                    break;
*/
                default:
                    errorMessage = "System error, please try later.";
                    break;
            }
            throw new ParamInvalidException(errorMessage + "; email = " + email + "; password = " + password);
        }
        return response.dataFormat();
    }
}
