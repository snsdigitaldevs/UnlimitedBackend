package com.simonschuster.pimsleur.unlimited.services.vocabularies;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoResponseDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyListInfoDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.data.edt.vocabularies.VocabularyItem;
import com.simonschuster.pimsleur.unlimited.data.edt.vocabularies.VocabularyItemsResultData;
import com.simonschuster.pimsleur.unlimited.data.edt.vocabularies.VocabularyResponseFromEdt;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class VocabularyService {

    private static final Logger logger = LoggerFactory.getLogger(VocabularyService.class);

    private static final String loggerMessage = "Request failed, please check input and try again!";

    @Autowired
    private ApplicationConfiguration config;

    @Autowired
    private AppIdService appIdService;

    public VocabularyInfoResponseDTO saveVocabularyToEdt(VocabularyInfoBodyDTO vocabularyInfoBodyDTO, String storeDomain) throws UnsupportedEncodingException {
        String appId = appIdService.getAppId(storeDomain);
        String vocabularySourceString = getVocabularySourceString(vocabularyInfoBodyDTO);

        String parameters = String.format(config.getProperty("edt.api.addVocabItem.parameters"), appId,
                            vocabularyInfoBodyDTO.getCustomerId(),
                            vocabularyInfoBodyDTO.getCustomerId().concat("_").concat(vocabularyInfoBodyDTO.getSubUserId()),
                            vocabularyInfoBodyDTO.getProductCode(),
                            encodeString(vocabularyInfoBodyDTO.getLanguage()),
                            encodeString(vocabularyInfoBodyDTO.getTransliteration()),
                            encodeString(vocabularyInfoBodyDTO.getTranslation()),
                            encodeString(vocabularyInfoBodyDTO.getMp3FileName()),
                            new Date().getTime(),
                            vocabularySourceString);

        VocabularyResponseFromEdt vocabularyResponseFromEdt = requestVocabularyOperationToEdt(parameters);

        if (!vocabularyResponseFromEdt.getResultCode().equals(EdtResponseCode.RESULT_OK)) {
            logger.info("Request failed, please check vocabularyInfoBodyDTO and try again! " + JsonUtils.toJsonString(vocabularyInfoBodyDTO));
            return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.FAILED);
        }

        return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS);
    }


    public VocabularyInfoResponseDTO getSaveVocabularyList(String customerId, String subUserId, String productCode, String storeDomain) {
        String appId = appIdService.getAppId(storeDomain);

        String parameters = String.format(config.getProperty("edt.api.getVocabItems.parameters"), appId, customerId,
                                        customerId.concat("_").concat(subUserId), productCode);

        VocabularyResponseFromEdt vocabularyResponseFromEdt = requestVocabularyOperationToEdt(parameters);

        Integer resultCode = vocabularyResponseFromEdt.getResultCode();

        if (!resultCode.equals(EdtResponseCode.RESULT_OK) && !resultCode.equals(EdtResponseCode.RESULT_RECORD_NOT_FOUND)) {
            logger.info(loggerMessage);
            return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.FAILED);
        }

        List<VocabularyItem> vocabularyItemList = getVocabularyList(vocabularyResponseFromEdt.getVocabularyItemsResultData());

        return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS, vocabularyItemList);

    }

    public VocabularyInfoResponseDTO deleteVocabularies(String customerId, String subUserId, String productCode, List<String> languageList, String storeDomain) throws UnsupportedEncodingException {
        String appId = appIdService.getAppId(storeDomain);

        String parameters = String.format(config.getProperty("edt.api.deleteVocabItems.parameters"), appId, customerId,
                customerId.concat("_").concat(subUserId), productCode, encodeString(StringUtils.join(languageList, ",")));

        VocabularyResponseFromEdt vocabularyResponseFromEdt = requestVocabularyOperationToEdt(parameters);

        if (!vocabularyResponseFromEdt.getResultCode().equals(EdtResponseCode.RESULT_OK)) {
            logger.info(loggerMessage);
            return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.FAILED);
        }

        return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS);

    }

    public VocabularyInfoResponseDTO saveVocabulariesToEdt(VocabularyListInfoDTO vocabularyListInfoDTO, String storeDomain) throws UnsupportedEncodingException {
        String appId = appIdService.getAppId(storeDomain);

        String vocabularyItemsString = StringUtils.join(vocabularyListInfoDTO.getVocabularyItemList()
                .stream()
                .map(JsonUtils::toJsonString)
                .collect(Collectors.toList()), ",");

        String parameters = String.format(config.getProperty("edt.api.addVocabItems.parameters"), appId,
                                        vocabularyListInfoDTO.getCustomerId(),
                                        vocabularyListInfoDTO.getSubUserId(),
                                        vocabularyListInfoDTO.getProductCode(),
                                        new Date().getTime(),
                                        encodeString(vocabularyItemsString));

        VocabularyResponseFromEdt vocabularyResponseFromEdt = requestVocabularyOperationToEdt(parameters);

        if (!vocabularyResponseFromEdt.getResultCode().equals(EdtResponseCode.RESULT_OK)) {
            logger.info("Request failed, please check vocabularyListInfoDTO and try again! " + JsonUtils.toJsonString(vocabularyListInfoDTO));
            return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.FAILED);
        }

        return new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS);
    }

    private List<VocabularyItem> getVocabularyList(VocabularyItemsResultData vocabularyItemsResultData) {
        if (vocabularyItemsResultData != null) {
            return vocabularyItemsResultData.getVocabularyItemList()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(vocabularyItem -> {
                        String subUserId = vocabularyItem.getSubUserId();
                        String[] subUserIdArray = subUserId.split("_");
                        if (subUserIdArray.length == 2){
                            vocabularyItem.setSubUserId(subUserIdArray[1]);
                        }
                        return vocabularyItem;
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();

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

    private String encodeString(String str) throws UnsupportedEncodingException {
        if (str !=null) {
            return URLEncoder.encode(str, "UTF-8");
        }
        return null;
    }

    private VocabularyResponseFromEdt requestVocabularyOperationToEdt(String parameters) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_FORM_URLENCODED);

        return postToEdt(new HttpEntity<>(parameters, httpHeaders),
                config.getProperty("edt.api.vocabOperation.url"), VocabularyResponseFromEdt.class);

    }
}
