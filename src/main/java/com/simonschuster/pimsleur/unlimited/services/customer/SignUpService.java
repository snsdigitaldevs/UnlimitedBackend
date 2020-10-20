package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.constants.StoreDomainConstants;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp.SignUpBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp.SignUpDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.SignUp.SignUpEDT;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class SignUpService {
    public static final String EMAIL_ALREADY_REGISTERED_ERROR_MESSAGE = "This email is already registered.";
    public static final String PASSWORD_INVALID_MESSAGE = "Password must be at least 8 characters,  \" +\n" + "\"including: lower case letters,  upper case letters,  numbers,  and special characters.";
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
        String storeDomain = signUpBodyDTO.getStoreDomain().equals(StoreDomainConstants.ALEXA_STORE_DOMAIN) ?
                "ss_pu" : signUpBodyDTO.getStoreDomain();

        String email = signUpBodyDTO.getEmail();
        String password = signUpBodyDTO.getPassword();
        String countryCode = signUpBodyDTO.getCountryCode();

        String template = applicationConfiguration.getProperty("edt.api.signUp.parameters.signUp").replace("wpc=%s&", "");
        String queryStrings = "/?" + String.format(template, userName, appId, email, storeDomain, countryCode);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(
            UriComponentsBuilder.fromUriString(queryStrings).build().getQueryParams());
        params.add("wpc", password);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        SignUpEDT response = postToEdt(entity, url, SignUpEDT.class);

        if (!response.getResultCode().equals(1)) {
            String errorMessage;
            switch (response.getResultCode()) {
                case -3011:
                case -9240:
                    errorMessage = EMAIL_ALREADY_REGISTERED_ERROR_MESSAGE;
                    break;
                case -1:
                    errorMessage = PASSWORD_INVALID_MESSAGE;
                    break;
                default:
                    errorMessage = "System error, please try later.";
                    break;
            }
            throw new ParamInvalidException(errorMessage);
        }
        return response.dataFormat();
    }
}
