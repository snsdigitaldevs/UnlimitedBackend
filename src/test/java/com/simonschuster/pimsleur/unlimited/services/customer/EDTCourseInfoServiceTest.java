package com.simonschuster.pimsleur.unlimited.services.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class EDTCourseInfoServiceTest {
    @Autowired
    private EDTCourseInfoService edtCourseInfoService;

    @Test
    public void shouldGetCorrectResponseFromEDTService() throws Exception {
        //mock edt api response
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh"),
                eq(form("gccfs"), "[\"9781508243328\"]")
                ))
                .response(file("src/test/resources/edtProductInfoResponse.json"));

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos("9781508243328", "");
            assertThat(productInfo.getProductInfoFromPU().getResultCode(),is(1));
            assertNotNull(productInfo.getProductInfoFromPU().getResultData().getCourseConfigs());
            assertNotNull(productInfo.getProductInfoFromPU().getResultData().getMediaSets());

            MediaSet oneMediaSet = productInfo.getProductInfoFromPU().getResultData().getMediaSets().get("9781508243328");
            assertEquals("9781508243328_Mandarin_1_AD.csv", oneMediaSet.getMediaItems().get(0).getFilename());
            //Test Jackson convert between String and Integer, source data has two types in different places.
            assertEquals(new Integer(1), oneMediaSet.getMediaItems().get(0).getIsActive());
            assertEquals(new Integer(1), oneMediaSet.getMediaItems().get(6).getIsActive());
            assertEquals("0", oneMediaSet.getMediaItems().get(2).getFileSizeBytes());
            assertEquals("", oneMediaSet.getMediaItems().get(0).getFileSizeBytes());
        });
    }

    //TODO: need to confirm if the courseconfigs part of response for multiple courses is the same as the test resource file.
    @Test
    public void shouldGetCorrectResponseFromEDTServiceWithMultipleCourse() throws Exception {
        //mock edt api response
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh")
                ))
                .response(file("src/test/resources/edtProductInfoResponseWithMultipleCourses.json"));

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos("whatever", "");
            assertThat(productInfo.getProductInfoFromPU().getResultCode(),is(1));
            assertNotNull(productInfo.getProductInfoFromPU().getResultData().getCourseConfigs());
            assertEquals(2, productInfo.getProductInfoFromPU().getResultData().getCourseConfigs().size());
        });
    }

    @Test
    public void verifyCourseConfigStructureDefinitionIsCorrect() throws Exception {
        String courseConfigStringValueFromResponse = "{    \"version\": \"1.0\",    \"courseLangName\": \"Mandarin Chinese\",    \"courseDataPresent\": 1,    \"hasVirtualProduct\": 0,    \"hasKittedProducts\": 0,    \"virtualProductCode\": \"\",    \"kittedProductCodes\": [],    \"isMetadataEncrypted\": 1,    \"readingFontSize\": 12,    \"usesTransliteration\": 1,    \"supportsTransliterationMouseovers\": 0,    \"readingLessonConfig\": {        \"displayXlitHelpFromMetadata\": true,        \"showAlphabetChartLabel\": \"Show Pinyin Pronunciation Guide\",        \"showTranslitLabel\": \"Show Chinese Characters\",        \"showTranslitOptionEnabled\": true,        \"showTranslitByDefault\": true,        \"alphabetChartSize\": \"SMALL\",        \"alphabetChartPosition\": \"RIGHT\"    },    \"appDefines\": {        \"defaultTransliterationMode\": 0    },    \"isbnToCourseName\": {        \"9781508243328\": \"Pimsleur Mandarin Chinese Unlimited Level 1 Sample\",        \"9781442394872\": \"Pimsleur Mandarin Chinese Unlimited Level 1\",        \"9781508231431\": \"Pimsleur Mandarin Chinese Unlimited Level 1\",        \"9781442394889\": \"Pimsleur Mandarin Chinese Unlimited Level 2\",        \"9781442394896\": \"Pimsleur Mandarin Chinese Unlimited Level 3\",        \"9781442394902\": \"Pimsleur Mandarin Chinese Unlimited Level 4\",        \"9781508230823\": \"Pimsleur Mandarin Chinese Unlimited Level 5\",        \"9781508260257\": \"Pimsleur Mandarin Chinese Unlimited Level 1-2\",        \"9781442394919\": \"Pimsleur Mandarin Chinese Unlimited Level 2-4\",        \"9781442394926\": \"Pimsleur Mandarin Chinese Unlimited Level 3-4\",        \"9781442383265\": \"Pimsleur Mandarin Chinese Unlimited Level 1-4\",        \"9781508231424\": \"Pimsleur Mandarin Chinese Unlimited Level 1-4\",        \"9781508227939\": \"Pimsleur Mandarin Chinese Unlimited Level 1-5\"    },    \"kittedFileLists\": {        \"9781442383265\": [            \"9781442394872\",            \"9781442394889\",            \"9781442394896\",            \"9781442394902\"        ],        \"9781442394919\": [            \"9781442394889\",            \"9781442394896\",            \"9781442394902\"        ],        \"9781442394926\": [            \"9781442394896\",            \"9781442394902\"        ]    },    \"courseLevelDefs\": [        {            \"isDemo\": 1,            \"isbn13\": \"9781508243328\",            \"fileListLocation\": \"REMOTE\",            \"courseLangName\": \"Mandarin Chinese\",            \"courseLevelName\": \"Mandarin Chinese 1 Sample\",            \"mediaSetId\": 33100,            \"audioPath\": \"Mandarin Chinese Demo/audio/\",            \"mainLessonsThumbImagePath\": \"Mandarin Chinese Demo/images/thumb/\",            \"mainLessonsFullImagePath\": \"Mandarin Chinese Demo/images/full/\",            \"timeCodesDataPath\": \"Mandarin Chinese Demo/metadata/timecode/\",            \"mediaSetDataFile\": \"9781508243328_Mandarin_Chinese_1.json\",            \"requiredDiskSpace\": 685519431,            \"filesToIgnoreForExport\": []        },        {            \"isDemo\": 0,            \"isbn13\": \"9781442394872\",            \"fileListLocation\": \"REMOTE\",            \"courseLangName\": \"Mandarin Chinese\",            \"courseLevelName\": \"Mandarin Chinese Level 1\",            \"mediaSetId\": 33000,            \"audioPath\": \"Mandarin Chinese I/audio/\",            \"mainLessonsThumbImagePath\": \"Mandarin Chinese I/images/thumb/\",            \"mainLessonsFullImagePath\": \"Mandarin Chinese I/images/full/\",            \"timeCodesDataPath\": \"Mandarin Chinese I/metadata/timecode/\",            \"mediaSetDataFile\": \"9781442394872_Mandarin_Chinese_1.json\",            \"requiredDiskSpace\": 685519431,            \"filesToIgnoreForExport\": [                \"9781442394872_Mandarin_Chinese1_Unit01_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit02_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit03_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit04_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit05_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit06_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit07_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit08_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit09_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit10_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit11_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit12_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit13_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit14_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit15_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit16_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit17_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit18_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit19_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit20_Reading.mp3\"            ]        },        {            \"isDemo\": 0,            \"isbn13\": \"9781508231431\",            \"fileListLocation\": \"REMOTE\",            \"courseLangName\": \"Mandarin Chinese\",            \"courseLevelName\": \"Mandarin Chinese Level 1\",            \"mediaSetId\": 33000,            \"audioPath\": \"Mandarin Chinese I/audio/\",            \"mainLessonsThumbImagePath\": \"Mandarin Chinese I/images/thumb/\",            \"mainLessonsFullImagePath\": \"Mandarin Chinese I/images/full/\",            \"timeCodesDataPath\": \"Mandarin Chinese I/metadata/timecode/\",            \"mediaSetDataFile\": \"9781508231431_Mandarin_Chinese_1.json\",            \"requiredDiskSpace\": 685519431,            \"filesToIgnoreForExport\": [                \"9781442394872_Mandarin_Chinese1_Unit01_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit02_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit03_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit04_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit05_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit06_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit07_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit08_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit09_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit10_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit11_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit12_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit13_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit14_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit15_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit16_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit17_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit18_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit19_Reading.mp3\",                \"9781442394872_Mandarin_Chinese1_Unit20_Reading.mp3\"            ]        },        {            \"isDemo\": 0,            \"isbn13\": \"9781442394889\",            \"fileListLocation\": \"REMOTE\",            \"courseLangName\": \"Mandarin Chinese\",            \"courseLevelName\": \"Mandarin Chinese Level 2\",            \"mediaSetId\": 34000,            \"audioPath\": \"Mandarin Chinese II/audio/\",            \"mainLessonsThumbImagePath\": \"Mandarin Chinese II/images/thumb/\",            \"mainLessonsFullImagePath\": \"Mandarin Chinese II/images/full/\",            \"timeCodesDataPath\": \"Mandarin Chinese II/metadata/timecode/\",            \"mediaSetDataFile\": \"9781442394889_Mandarin_Chinese_2.json\",            \"requiredDiskSpace\": 650803956,            \"filesToIgnoreForExport\": [                \"9781442394889_Mandarin_Chinese2_Unit01_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit02_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit03_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit04_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit05_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit06_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit07_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit08_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit09_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit10_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit11_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit12_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit13_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit14_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit15_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit16_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit17_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit18_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit19_Reading.mp3\",                \"9781442394889_Mandarin_Chinese2_Unit20_Reading.mp3\"            ]        },        {            \"isDemo\": 0,            \"isbn13\": \"9781442394896\",            \"fileListLocation\": \"REMOTE\",            \"courseLangName\": \"Mandarin Chinese\",            \"courseLevelName\": \"Mandarin Chinese Level 3\",            \"mediaSetId\": 35000,            \"audioPath\": \"Mandarin Chinese III/audio/\",            \"mainLessonsThumbImagePath\": \"Mandarin Chinese III/images/thumb/\",            \"mainLessonsFullImagePath\": \"Mandarin Chinese III/images/full/\",            \"timeCodesDataPath\": \"Mandarin Chinese III/metadata/timecode/\",            \"mediaSetDataFile\": \"9781442394896_Mandarin_Chinese_3.json\",            \"requiredDiskSpace\": 643893756,            \"filesToIgnoreForExport\": [                \"9781442394896_Mandarin_Chinese3_Unit01_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit02_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit03_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit04_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit05_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit06_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit07_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit08_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit09_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit10_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit11_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit12_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit13_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit14_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit15_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit16_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit17_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit18_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit19_Reading.mp3\",                \"9781442394896_Mandarin_Chinese3_Unit20_Reading.mp3\"            ]        },        {            \"isDemo\": 0,            \"isbn13\": \"9781442394902\",            \"fileListLocation\": \"REMOTE\",            \"courseLangName\": \"Mandarin Chinese\",            \"courseLevelName\": \"Mandarin Chinese Level 4\",            \"mediaSetId\": 36000,            \"audioPath\": \"Mandarin Chinese IV/audio/\",            \"mainLessonsThumbImagePath\": \"Mandarin Chinese IV/images/thumb/\",            \"mainLessonsFullImagePath\": \"Mandarin Chinese IV/images/full/\",            \"timeCodesDataPath\": \"Mandarin Chinese IV/metadata/timecode/\",            \"mediaSetDataFile\": \"9781442394902_Mandarin_Chinese_4.json\",            \"requiredDiskSpace\": 643893756,            \"filesToIgnoreForExport\": [                \"9781442394902_Mandarin_Chinese4_Unit01_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit02_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit03_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit04_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit05_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit06_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit07_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit08_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit09_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit10_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit11_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit12_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit13_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit14_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit15_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit16_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit17_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit18_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit19_Reading.mp3\",                \"9781442394902_Mandarin_Chinese4_Unit20_Reading.mp3\"            ]        },        {            \"isDemo\": 0,            \"isbn13\": \"9781508230823\",            \"fileListLocation\": \"REMOTE\",            \"courseLangName\": \"Mandarin Chinese\",            \"courseLevelName\": \"Mandarin Chinese Level 5\",            \"mediaSetId\": 50000,            \"audioPath\": \"Mandarin Chinese V/audio/\",            \"mainLessonsThumbImagePath\": \"Mandarin Chinese V/images/thumb/\",            \"mainLessonsFullImagePath\": \"Mandarin Chinese V/images/full/\",            \"timeCodesDataPath\": \"Mandarin Chinese V/metadata/timecode/\",            \"mediaSetDataFile\": \"9781508230823_Mandarin_Chinese_5.json\",            \"requiredDiskSpace\": 615700410,            \"filesToIgnoreForExport\": [                \"9781508230823_Mandarin_Chinese5_Unit01_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit02_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit03_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit04_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit05_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit06_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit07_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit08_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit09_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit10_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit11_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit12_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit13_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit14_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit15_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit16_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit17_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit18_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit19_Reading.mp3\",                \"9781508230823_Mandarin_Chinese5_Unit20_Reading.mp3\"            ],            \"userPrefs\":{\"PREF_READING_INTRO_PLAYED\":{\"key\":\"READING_INTRO_PLAYED_9781508230823\",\"domain\":\"COURSE_LEVEL_PREF\",\"hasDefaultAppDefine\":false,\"valueType\":\"boolean\",\"resetValue\":false}},            \"appDefines\": {                \"SCREEN_INFO\": {                    \"SCREEN_TAB_READING_LESSON\": {                        \"title\": \"Level 5 Reading Lessons\",                        \"audioQueKeyIntroFileName\": \"mandarin_5_reading_intro.mp3\"                    }                }            }        }    ]}";
        ObjectMapper mapper = new ObjectMapper();
        CourseConfig courseConfig = mapper.readValue(courseConfigStringValueFromResponse, new TypeReference<CourseConfig>() {});

        assertEquals("1.0", courseConfig.getVersion());
        assertEquals("Mandarin Chinese", courseConfig.getCourseLangName());
        assertNotNull(courseConfig.getAppDefines());
        assertEquals("Show Pinyin Pronunciation Guide", courseConfig.getReadingLessonConfig().getShowAlphabetChartLabel());
        assertEquals("Pimsleur Mandarin Chinese Unlimited Level 1 Sample", courseConfig.getIsbnToCourseName().get("9781508243328"));
        assertEquals(3, courseConfig.getKittedFileLists().size());
        assertEquals(new Integer(1), courseConfig.getCourseLevelDefs().get(0).getIsDemo());
    }

    @Test
    public void shouldGetProductInfoFromPCMCorrectlyWhenNoDataFromPU() throws Exception {
        //mock edt api response
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/qwsfrecv.php")),
                eq(form("action"), "fgtyh"),
                eq(form("gccfs"), "[\"9781508243328\"]")
        ))
                .response("{\"result_code\":-1}");

        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rsovkolfqxrjl.php")),
                eq(form("action"), "pcm_blmqide"))
                )
                .response(file("src/test/resources/pcmCustInfoResponse.json"));

        running(server, () -> {
            AggregatedProductInfo productInfo = edtCourseInfoService.getCourseInfos("9781508243328", "auth0_user_id");

            assertNull(productInfo.getProductInfoFromPU());
            assertNotNull(productInfo.getProductInfoFromPCM());

        });
    }
}