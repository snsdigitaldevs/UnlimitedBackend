package com.simonschuster.pimsleur.unlimited.services.freeLessons;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.LearnerInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.CodeOnlyResposeEDT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class LearnerInfoService {
    @Autowired
    private ApplicationConfiguration config;

    public void LearnerInfo(LearnerInfoBodyDTO learnerInfoBodyDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> parameters = new HttpEntity<>(String.format(config.getProperty("edt.api.freeLesson.parameters"),
                learnerInfoBodyDTO.getSource(), learnerInfoBodyDTO.getCountryCode(), learnerInfoBodyDTO.getEmail(), learnerInfoBodyDTO.getLanguageName()
        ), headers);
        String url = config.getProperty("edt.api.freeLesson.url");
        CodeOnlyResposeEDT learnerInfoResponse = postToEdt(parameters, url, CodeOnlyResposeEDT.class);
        if (!learnerInfoResponse.getResultCode().equals(1)) {
            throw new PimsleurException("send learner info to EDT failed!");
        }
    }
}
