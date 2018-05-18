package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp.SignUpBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp.SignUpDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.SignUp.SignUpEDT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class SignUpService {
    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    public SignUpDTO signUp(SignUpBodyDTO signUpBodyDTO) {
        String url = applicationConfiguration.getProperty("edt.api.signUp.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(String.format(
                applicationConfiguration.getProperty("edt.api.signUp.parameters.signUp"),
                signUpBodyDTO.getPassword(), signUpBodyDTO.getEmail(), signUpBodyDTO.getStoreDomain(), signUpBodyDTO.getCountryCode()
        ), headers);
        SignUpEDT response = postToEdt(entity, url, SignUpEDT.class);

        if (!response.getResultCode().equals(1)) {
            String errorMessage;
            switch (response.getResultCode()) {
                case -3011:
                    errorMessage = "This email is already registered.";
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
                    errorMessage = "System error, please try latter.";
                    break;
            }
            throw new ParamInvalidException(errorMessage);
        }
        return response.dataFormat();
    }
}
