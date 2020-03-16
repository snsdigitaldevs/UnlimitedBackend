package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoResponseDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyItemDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyListInfoDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.vocabularies.VocabularyItemFromEdt;
import com.simonschuster.pimsleur.unlimited.services.vocabularies.VocabularyService;
import com.simonschuster.pimsleur.unlimited.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VocabularyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VocabularyService vocabularyService;

    @Test
    public void should_save_vocabulary_success_when_call_save_vocabulary_api_given_a_valid_vocabulary_info_body() throws Exception {
        VocabularyInfoBodyDTO vocabularyInfoBodyDTO = new VocabularyInfoBodyDTO()
                                        .setCustomerId("118950")
                                        .setSubUserId("5ae0ced61cb1f")
                                        .setProductCode("9781508235972")
                                        .setLanguage("test")
                                        .setTransliteration("some_transliteration_text")
                                        .setMp3FileName("123.mp3")
                                        .setPackGroupNumber(1);
        List<VocabularyItemFromEdt> vocabularyItemFromEdtList = mockVocabularyItemList();

        VocabularyInfoResponseDTO vocabularyInfoResponseDTO =
                new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS);

        when(vocabularyService.saveVocabularyToEdt(any(), any())).thenReturn(vocabularyInfoResponseDTO);
        mockMvc.perform(post("/puProduct/vocabulary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJsonString(vocabularyInfoBodyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(VocabularyInfoResponseDTO.SUCCESS));
    }

    @Test
    public void should_save_vocabulary_failed_when_call_save_vocabulary_api_given_customerId_null_value() throws Exception {
        VocabularyInfoBodyDTO vocabularyInfoBodyDTO = new VocabularyInfoBodyDTO()
                                                        .setCustomerId(null)
                                                        .setSubUserId("5ae0ced61cb1f")
                                                        .setProductCode("9781508235972")
                                                        .setLanguage("test")
                                                        .setTransliteration("some_transliteration_text")
                                                        .setMp3FileName("123.mp3")
                                                        .setPackGroupNumber(1);

        mockMvc.perform(post("/puProduct/vocabulary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJsonString(vocabularyInfoBodyDTO)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void should_save_vocabulary_failed_when_call_save_vocabulary_api_given_language_empty_value() throws Exception {
        VocabularyInfoBodyDTO vocabularyInfoBodyDTO = new VocabularyInfoBodyDTO()
                .setCustomerId("118950")
                .setSubUserId("5ae0ced61cb1f")
                .setProductCode("9781508235972")
                .setLanguage("")
                .setTransliteration("some_transliteration_text")
                .setMp3FileName("123.mp3")
                .setPackGroupNumber(1);

        mockMvc.perform(post("/puProduct/vocabulary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJsonString(vocabularyInfoBodyDTO)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void should_get_vocabulary_list_success_when_call_get_vocabulary_list_api_given_valid_customer_info_and_product_code() throws Exception {
        List<VocabularyItemFromEdt> vocabularyItemFromEdtList = mockVocabularyItemList();

        VocabularyInfoResponseDTO vocabularyInfoResponseDTO =
                new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS, vocabularyItemFromEdtList);

        when(vocabularyService.getSaveVocabularyList("118950", "5ae0ced61cb1f", "9781508235972", null ))
                .thenReturn(vocabularyInfoResponseDTO);


        mockMvc.perform(get("/puProduct/vocabulary")
                    .param("customerId", "118950")
                    .param("subUserId", "5ae0ced61cb1f")
                    .param("productCode", "9781508235972"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(VocabularyInfoResponseDTO.SUCCESS))
                .andExpect(jsonPath("$.vocabularyItemFromEdtList[0].customerId").value("118950"))
                .andExpect(jsonPath("$.vocabularyItemFromEdtList[0].subUserId").value("5ae0ced61cb1f"))
                .andExpect(jsonPath("$.vocabularyItemFromEdtList[0].productCode").value("9781508235972"))
                .andExpect(jsonPath("$.vocabularyItemFromEdtList[0].language").value("test"))
                .andExpect(jsonPath("$.vocabularyItemFromEdtList[0].transliteration").value("some_transliteration_text"))
                .andExpect(jsonPath("$.vocabularyItemFromEdtList[0].mp3FileName").value("123.mp3"))
                .andExpect(jsonPath("$.vocabularyItemFromEdtList[0].packGroupNumber").value(1));
    }

    @Test
    public void should_get_vocabulary_list_failed_when_call_get_vocabulary_list_api_given_null_productCode() throws Exception {

        mockMvc.perform(get("/puProduct/vocabulary")
                .param("customerId", "118950")
                .param("subUserId", "5ae0ced61cb1f"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void should_delete_vocabularies_success_when_delete_vocabularies_api_given_valid_input() throws Exception {
        VocabularyInfoResponseDTO vocabularyInfoResponseDTO =
                new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS);
        List<String> languageList = Arrays.asList("myword1", "myword2");

        when(vocabularyService.deleteVocabularies(any(), any(), any(), any(), any()))
                .thenReturn(vocabularyInfoResponseDTO);

        mockMvc.perform(delete("/puProduct/vocabulary")
                .param("customerId", "118950")
                .param("subUserId", "5ae0ced61cb1f")
                .param("productCode", "9781508235972")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJsonString(languageList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(VocabularyInfoResponseDTO.SUCCESS));
    }

    @Test
    public void should_delete_vocabularies_failed_when_delete_vocabularies_api_given_invalid_input() throws Exception {


        mockMvc.perform(delete("/puProduct/vocabulary")
                .param("customerId", "118950")
                .param("subUserId", "5ae0ced61cb1f"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void should_save_vocabularies_success_when_call_save_vocabularies_api_given_a_valid_vocabulary_info_body() throws Exception {
        VocabularyListInfoDTO vocabularyListInfoDTO = mockVocabularyListInfoDTO();

        VocabularyInfoResponseDTO vocabularyInfoResponseDTO =
                new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS);

        when(vocabularyService.saveVocabulariesToEdt(any(), any())).thenReturn(vocabularyInfoResponseDTO);
        mockMvc.perform(post("/puProduct/vocabularies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJsonString(vocabularyListInfoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(VocabularyInfoResponseDTO.SUCCESS));
    }

    @Test
    public void should_save_vocabularies_failed_when_call_save_vocabularies_api_given_a_invalid_vocabulary_info_body() throws Exception {
        VocabularyListInfoDTO vocabularyListInfoDTO = new VocabularyListInfoDTO()
                            .setCustomerId("118950")
                            .setSubUserId("5ae0ced61cb1f");

        mockMvc.perform(post("/puProduct/vocabularies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJsonString(vocabularyListInfoDTO)))
                .andExpect(status().is5xxServerError());
    }

    private List<VocabularyItemFromEdt> mockVocabularyItemList() {
        VocabularyItemFromEdt vocabularyItemFromEdt = new VocabularyItemFromEdt();
        vocabularyItemFromEdt.setCustomerId("118950");
        vocabularyItemFromEdt.setSubUserId("5ae0ced61cb1f");
        vocabularyItemFromEdt.setProductCode("9781508235972");
        vocabularyItemFromEdt.setLanguage("test");
        vocabularyItemFromEdt.setTransliteration("some_transliteration_text");
        vocabularyItemFromEdt.setMp3FileName("123.mp3");
        vocabularyItemFromEdt.setPackGroupNumber(1);
        List<VocabularyItemFromEdt> vocabularyItemFromEdtList = new ArrayList<>();
        vocabularyItemFromEdtList.add(vocabularyItemFromEdt);
        return vocabularyItemFromEdtList;
    }

    private VocabularyListInfoDTO mockVocabularyListInfoDTO() {
        VocabularyItemDTO vocabularyItemDTO = new VocabularyItemDTO()
                                            .setLanguage("对不起")
                                            .setTransliteration("duì bù qĭ.")
                                            .setTranslation("duì bù qĭ.")
                                            .setTransliteration("https://install.pimsleurunlimited.com/staging_n/common/mandarinchinese/Mandarin Chinese I/audio/9781442394872_REVIEW_AUDIO_SNIPPETS/quiz/9781442394872_Mandarin_Chinese_1_QZ_002.mp3")
                                            .setLessonNumber(1);

        List<VocabularyItemDTO> vocabularyItemDTOList = Arrays.asList(vocabularyItemDTO);
        return new VocabularyListInfoDTO()
                                        .setCustomerId("118950")
                                        .setSubUserId("5ae0ced61cb1f")
                                        .setProductCode("9781508235972")
                                        .setVocabularyItemList(vocabularyItemDTOList);

    }
}
