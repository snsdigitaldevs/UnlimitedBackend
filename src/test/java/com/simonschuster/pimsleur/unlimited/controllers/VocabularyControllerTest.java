package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoResponseDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.vocabularies.VocabularyItem;
import com.simonschuster.pimsleur.unlimited.services.vocabularies.VocabularyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
        VocabularyItem vocabularyItem = new VocabularyItem();
        vocabularyItem.setCustomerId("118950");
        vocabularyItem.setSubUserId("5ae0ced61cb1f");
        vocabularyItem.setProductCode("9781508235972");
        vocabularyItem.setLanguage("test");
        vocabularyItem.setTransliteration("some_transliteration_text");
        vocabularyItem.setMp3FileName("123.mp3");
        vocabularyItem.setPackGroupNumber(1);
        List<VocabularyItem> vocabularyItemList = new ArrayList<>();
        vocabularyItemList.add(vocabularyItem);

        VocabularyInfoResponseDTO vocabularyInfoResponseDTO =
                new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS, vocabularyItemList);

        when(vocabularyService.saveVocabularyToEdt(any(), any())).thenReturn(vocabularyInfoResponseDTO);
        mockMvc.perform(post("/puProduct/vocabulary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(vocabularyInfoBodyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(VocabularyInfoResponseDTO.SUCCESS))
                .andExpect(jsonPath("$.vocabularyItemList.length()").value(1))
                .andExpect(jsonPath("$.vocabularyItemList[0].customerId").value(vocabularyItem.getCustomerId()))
                .andExpect(jsonPath("$.vocabularyItemList[0].subUserId").value(vocabularyItem.getSubUserId()))
                .andExpect(jsonPath("$.vocabularyItemList[0].productCode").value(vocabularyItem.getProductCode()))
                .andExpect(jsonPath("$.vocabularyItemList[0].language").value(vocabularyItem.getLanguage()))
                .andExpect(jsonPath("$.vocabularyItemList[0].packGroupNumber").value(vocabularyItem.getPackGroupNumber()));
    }

    @Test
    public void should_save_vocabulary_faild_when_call_save_vocabulary_api_given_customerId_null_value() throws Exception {
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
                .content(JSONObject.toJSONString(vocabularyInfoBodyDTO)))
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
                .content(JSONObject.toJSONString(vocabularyInfoBodyDTO)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void should_get_vocabulary_list_success_when_call_get_vocabulary_list_api_given_valid_customer_info_and_product_code() throws Exception {
        VocabularyItem vocabularyItem = new VocabularyItem();
        vocabularyItem.setCustomerId("118950");
        vocabularyItem.setSubUserId("5ae0ced61cb1f");
        vocabularyItem.setProductCode("9781508235972");
        vocabularyItem.setLanguage("test");
        vocabularyItem.setTransliteration("some_transliteration_text");
        vocabularyItem.setMp3FileName("123.mp3");
        vocabularyItem.setPackGroupNumber(1);
        List<VocabularyItem> vocabularyItemList = new ArrayList<>();
        vocabularyItemList.add(vocabularyItem);

        VocabularyInfoResponseDTO vocabularyInfoResponseDTO =
                new VocabularyInfoResponseDTO(VocabularyInfoResponseDTO.SUCCESS, vocabularyItemList);

        when(vocabularyService.getSaveVocabularyList("118950", "5ae0ced61cb1f", "9781508235972", null ))
                .thenReturn(vocabularyInfoResponseDTO);


        mockMvc.perform(get("/puProduct/vocabulary")
                    .param("customerId", "118950")
                    .param("subUserId", "5ae0ced61cb1f")
                    .param("productCode", "9781508235972"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_vocabulary_list_failed_when_call_get_vocabulary_list_api_given_null_productCode() throws Exception {

        mockMvc.perform(get("/puProduct/vocabulary")
                .param("customerId", "118950")
                .param("subUserId", "5ae0ced61cb1f"))
                .andExpect(status().is4xxClientError());
    }
}
