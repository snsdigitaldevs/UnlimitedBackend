package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoResponseDTO;
import com.simonschuster.pimsleur.unlimited.services.vocabularies.VocabularyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/puProduct/vocabulary")
public class VocabularyController {

    @Autowired
    private VocabularyService vocabularyService;

    @ApiOperation(value = "save vocabulary for PU course",
            notes = "save a vocabulary by one time for PU course")
    @PostMapping
    public VocabularyInfoResponseDTO saveVocabulary(@Valid @RequestBody VocabularyInfoBodyDTO vocabularyInfoBodyDTO,
                                                    @RequestParam(required = false) String storeDomain) throws UnsupportedEncodingException {
        return vocabularyService.saveVocabularyToEdt(vocabularyInfoBodyDTO, storeDomain);
    }

    @ApiOperation(value = "get vocabulary list for PU course",
            notes = "get vocabulary list by customerId, subUserId and productCode")
    @GetMapping
    public VocabularyInfoResponseDTO getVocabularyList(@RequestParam String customerId,
                                                    @RequestParam String subUserId,
                                                    @RequestParam String productCode,
                                                    @RequestParam(required = false) String storeDomain) {
        return vocabularyService.getSaveVocabularyList(customerId, subUserId, productCode, storeDomain);
    }

    @ApiOperation(value = "delete vocabularies for PU course",
            notes = "delete vocabularies by customerId, subUserId, productCode and language array")
    @DeleteMapping
    public VocabularyInfoResponseDTO deleteVocabularies(@RequestParam String customerId,
                                                       @RequestParam String subUserId,
                                                       @RequestParam String productCode,
                                                       @RequestBody List<String> languageList,
                                                       @RequestParam(required = false) String storeDomain) {
        return vocabularyService.deleteVocabularies(customerId, subUserId, productCode, languageList, storeDomain);
    }
}
