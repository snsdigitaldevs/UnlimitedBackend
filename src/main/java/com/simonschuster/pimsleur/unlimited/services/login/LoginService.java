package com.simonschuster.pimsleur.unlimited.services.login;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.auth0.Auth0TokenInfo;
import com.simonschuster.pimsleur.unlimited.data.auth0.UserInfoFromAuth0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static org.springframework.http.HttpMethod.GET;

@Service
public class LoginService {

    @Autowired
    private ApplicationConfiguration config;

    public String getAuthorizationSub(String userName, String password) {
        String authorization = requestToGetAuthorization(userName, password);
        return requestToGetSub(authorization);
    }

    public Auth0TokenInfo getAuthorizationInfoFromAuth0(String userName, String password) {
        return requestToGetAuth0TokenInfo(userName, password);
    }

    private String requestToGetAuthorization(String userName, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Auth0TokenInfo auth0TokenInfo = postToEdt(
                new HttpEntity<>(
                        String.format(config.getAuth0ApiParameter("tokenApiParameters"),
                                userName,
                                password),
                        headers),
                config.getProperty("oauth.login.tokenApiUrl"),
                Auth0TokenInfo.class);
        return auth0TokenInfo.getToken_type() + " " + auth0TokenInfo.getAccess_token();
    }

    private Auth0TokenInfo requestToGetAuth0TokenInfo(String userName, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return postToEdt(
                new HttpEntity<>(
                        String.format(config.getAuth0ApiParameter("tokenApiParameters"),
                                userName,
                                password),
                        headers),
                config.getProperty("oauth.login.tokenApiUrl"),
                Auth0TokenInfo.class);
    }

    private String requestToGetSub(String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", authorization);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<UserInfoFromAuth0> responseEntity = restTemplate.exchange(config.getProperty("oauth.login.userInfoApiUrl"),
                GET, entity, UserInfoFromAuth0.class);

        return responseEntity.getBody().getSub();
    }
}
