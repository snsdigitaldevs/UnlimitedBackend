package com.simonschuster.pimsleur.unlimited.services.customer;

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

        String userName = signUpBodyDTO.getUserName() == null ? "" : "&worf=" + signUpBodyDTO.getUserName();
        HttpEntity<String> entity = new HttpEntity<>(String.format(
                applicationConfiguration.getProperty("edt.api.signUp.parameters.signUp"),
                userName, signUpBodyDTO.getPassword(), signUpBodyDTO.getEmail(), signUpBodyDTO.getStoreDomain(), signUpBodyDTO.getCountryCode()
        ), headers);
        return postToEdt(entity, url, SignUpEDT.class).dataFormat();
    }
}
