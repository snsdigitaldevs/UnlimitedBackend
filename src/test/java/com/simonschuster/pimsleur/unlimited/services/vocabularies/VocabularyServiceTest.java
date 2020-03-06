package com.simonschuster.pimsleur.unlimited.services.vocabularies;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyInfoResponseDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class VocabularyServiceTest {

    @Autowired
    private VocabularyService vocabularyService;

    @Test
    public void should_returned_success_status_and_corresponding_vocabularies_list_when_call_save_vocabulary_method_given_valid_vocabulary_info() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/gnsrs.php")),
                eq(form("action"), "afiva")))
                .response(file("src/test/resources/saveVocabularySuccessResponse.json"));

        running(server, () -> {
            VocabularyInfoBodyDTO vocabularyInfoBodyDTO = new VocabularyInfoBodyDTO("118950", "5ae0ced61cb1f", "9781508235972", "test", "some_transliteration_text", "", "123.mp3",1, null);
            VocabularyInfoResponseDTO response = vocabularyService.saveVocabularyToEdt(vocabularyInfoBodyDTO, null);

            assertEquals(VocabularyInfoResponseDTO.SUCCESS, response.getStatus());
            assertEquals("118950", response.getVocabularyItemList().get(0).getCustomerId());
            assertEquals("5ae0ced61cb1f", response.getVocabularyItemList().get(0).getSubUserId());
            assertEquals("9781508235972", response.getVocabularyItemList().get(0).getProductCode());
            assertEquals("test", response.getVocabularyItemList().get(0).getLanguage());

        });

    }

    @Test
    public void should_returned_correct_failed_status_when_call_save_vocabulary_method_given_invalid_vocabulary_info() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/gnsrs.php")),
                eq(form("action"), "afiva")))
                .response(file("src/test/resources/saveVocabularyFailedResponse.json"));

        running(server, () -> {
            VocabularyInfoBodyDTO vocabularyInfoBodyDTO = new VocabularyInfoBodyDTO("118950", "118950", "9781508235972", "test", "some_transliteration_text", "", "123.mp3",1, null);
            VocabularyInfoResponseDTO response = vocabularyService.saveVocabularyToEdt(vocabularyInfoBodyDTO, null);

            assertEquals(VocabularyInfoResponseDTO.FAILED, response.getStatus());

        });

    }
}
