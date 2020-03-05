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
    public void saveVocabulary() throws Exception {
        VocabularyInfoBodyDTO vocabularyInfoBodyDTO = new VocabularyInfoBodyDTO("118950", "5ae0ced61cb1f", "9781508235972", "test", "some_transliteration_text", "", "123.mp3",null, 1);
        VocabularyItem vocabularyItem = new VocabularyItem("118950", "5ae0ced61cb1f", "9781508235972", "myword3", "some_transliteration", "some_translation", "some_mp3_file_name", null, 1, 1534567890987L);
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

}
