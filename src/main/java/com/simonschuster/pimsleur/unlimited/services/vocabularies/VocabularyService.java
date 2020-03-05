package com.simonschuster.pimsleur.unlimited.services.vocabularies;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoResponseDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.data.edt.vocabularies.VocabularyResponseFromEdt;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class VocabularyService {

    @Autowired
    private ApplicationConfiguration config;

    @Autowired
    private AppIdService appIdService;

    private static final Logger logger = LoggerFactory.getLogger(VocabularyService.class);

    public VocabularyInfoResponseDTO saveVocabularyToEdt(VocabularyInfoBodyDTO vocabularyInfoBodyDTO, String storeDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_FORM_URLENCODED);
        String appId = appIdService.getAppId(storeDomain);
        String vocabularySourceString = getVocabularySourceString(vocabularyInfoBodyDTO);

        String parameters = String.format(config.getProperty("edt.api.addVocabItem.parameters"), appId,
                            vocabularyInfoBodyDTO.getCustomerId(),
                            vocabularyInfoBodyDTO.getSubUserId(),
                            vocabularyInfoBodyDTO.getProductCode(),
                            vocabularyInfoBodyDTO.getLanguage(),
                            vocabularyInfoBodyDTO.getTransliteration(),
                            vocabularyInfoBodyDTO.getTranslation(),
                            vocabularyInfoBodyDTO.getMp3FileName(),
                            new Date().getTime(),
                            vocabularySourceString);

        VocabularyResponseFromEdt vocabularyResponseFromEdt = postToEdt(new HttpEntity<>(parameters, httpHeaders),
                        config.getProperty("edt.api.addVocabItem.url"), VocabularyResponseFromEdt.class);

        if (!vocabularyResponseFromEdt.getResultCode().equals(EdtResponseCode.RESULT_OK)) {
            logger.info("Request failed, please check vocabularyInfoBodyDTO and try again!");
            return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.FAILED);
        }

        return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS, vocabularyResponseFromEdt.getVocabularyItemsResultData().getVocabularyItemList());
    }

    private String getVocabularySourceString(VocabularyInfoBodyDTO vocabularyInfoBodyDTO) {
        Integer lessonNumber = vocabularyInfoBodyDTO.getLessonNumber();
        Integer packGroupNumber = vocabularyInfoBodyDTO.getPackGroupNumber();

        if (lessonNumber != null && packGroupNumber == null) {
            String lessonNumberKey = config.getProperty("edt.api.addVocabItem.lessonNumber.key");
            return "&".concat(lessonNumberKey).concat("=").concat(String.valueOf(lessonNumber));
        }

        if (packGroupNumber != null && lessonNumber == null) {
            String packGroupNumberKey = config.getProperty("edt.api.addVocabItem.packageGroupNumber.key");
            return "&".concat(packGroupNumberKey).concat("=").concat(String.valueOf(packGroupNumber));
        }
        return "";
    }
}
