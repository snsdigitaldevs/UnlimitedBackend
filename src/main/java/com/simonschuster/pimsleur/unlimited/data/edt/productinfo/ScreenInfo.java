package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScreenInfo {
    @JsonProperty("SCREEN_TAB_READING_LESSON")
    private ScreenTabReadingLesson screenTabReadingLesson;
    /**
     * SCREEN_HOME : {"title":"Bienvenido a Pimsleur Unlimited"}
     * SCREEN_TAB_MAIN_LESSON : {"title":"Lecciones de audio diarias"}
     * SCREEN_REVIEW : {"title":"Repasar Introducción"}
     * SCREEN_QUIZ : {"title":"Hable Fácil"}
     * SCREEN_FLASHCARDS : {"title":"Tarjetas"}
     * SCREEN_ACTIVATION : {"title":"Activación Pantalla"}
     * SCREEN_TAB_VC : {"id":6,"title":"Hable Fácil","audioQueKeyLeadin":"SPEAK_EASY_LEAD_IN","audioQueKeyLeadinFileName":"speak_easy_leadin.mp3","audioQueLeadin1TimeCodeStart":"0:00.000","audioQueLeadin1TimeCodeEnd":"0:08.020","audioQueLeadin2TimeCodeStart":"0:08.020","audioQueLeadin2TimeCodeEnd":"0:34.743","audioQueLeadin3TimeCodeStart":"0:34.743","audioQueLeadin3TimeCodeEnd":"1:34.955"}
     * SCREEN_ENGAGE : {"title":"Pimsleur Participar","hasIntro":1,"audioQueKeyIntroFileName":"engage_intro.mp3"}
     */

    private ScreenTabReadingLesson SCREEN_HOME;
    private ScreenTabReadingLesson SCREEN_TAB_MAIN_LESSON;
    private ScreenTabReadingLesson SCREEN_REVIEW;
    private ScreenTabReadingLesson SCREEN_QUIZ;
    private ScreenTabReadingLesson SCREEN_FLASHCARDS;
    private ScreenTabReadingLesson SCREEN_ACTIVATION;
    private SCREENTABVCBean SCREEN_TAB_VC;
    private SCREENENGAGEBean SCREEN_ENGAGE;

    public ScreenTabReadingLesson getScreenTabReadingLesson() {
        return screenTabReadingLesson;
    }

    public void setScreenTabReadingLesson(ScreenTabReadingLesson screenTabReadingLesson) {
        this.screenTabReadingLesson = screenTabReadingLesson;
    }

    public ScreenTabReadingLesson getSCREEN_HOME() {
        return SCREEN_HOME;
    }

    public void setSCREEN_HOME(ScreenTabReadingLesson SCREEN_HOME) {
        this.SCREEN_HOME = SCREEN_HOME;
    }

    public ScreenTabReadingLesson getSCREEN_TAB_MAIN_LESSON() {
        return SCREEN_TAB_MAIN_LESSON;
    }

    public void setSCREEN_TAB_MAIN_LESSON(ScreenTabReadingLesson SCREEN_TAB_MAIN_LESSON) {
        this.SCREEN_TAB_MAIN_LESSON = SCREEN_TAB_MAIN_LESSON;
    }

    public ScreenTabReadingLesson getSCREEN_REVIEW() {
        return SCREEN_REVIEW;
    }

    public void setSCREEN_REVIEW(ScreenTabReadingLesson SCREEN_REVIEW) {
        this.SCREEN_REVIEW = SCREEN_REVIEW;
    }

    public ScreenTabReadingLesson getSCREEN_QUIZ() {
        return SCREEN_QUIZ;
    }

    public void setSCREEN_QUIZ(ScreenTabReadingLesson SCREEN_QUIZ) {
        this.SCREEN_QUIZ = SCREEN_QUIZ;
    }

    public ScreenTabReadingLesson getSCREEN_FLASHCARDS() {
        return SCREEN_FLASHCARDS;
    }

    public void setSCREEN_FLASHCARDS(ScreenTabReadingLesson SCREEN_FLASHCARDS) {
        this.SCREEN_FLASHCARDS = SCREEN_FLASHCARDS;
    }

    public ScreenTabReadingLesson getSCREEN_ACTIVATION() {
        return SCREEN_ACTIVATION;
    }

    public void setSCREEN_ACTIVATION(ScreenTabReadingLesson SCREEN_ACTIVATION) {
        this.SCREEN_ACTIVATION = SCREEN_ACTIVATION;
    }

    public SCREENTABVCBean getSCREEN_TAB_VC() {
        return SCREEN_TAB_VC;
    }

    public void setSCREEN_TAB_VC(SCREENTABVCBean SCREEN_TAB_VC) {
        this.SCREEN_TAB_VC = SCREEN_TAB_VC;
    }

    public SCREENENGAGEBean getSCREEN_ENGAGE() {
        return SCREEN_ENGAGE;
    }

    public void setSCREEN_ENGAGE(SCREENENGAGEBean SCREEN_ENGAGE) {
        this.SCREEN_ENGAGE = SCREEN_ENGAGE;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SCREENTABVCBean {
        /**
         * id : 6
         * title : Hable Fácil
         * audioQueKeyLeadin : SPEAK_EASY_LEAD_IN
         * audioQueKeyLeadinFileName : speak_easy_leadin.mp3
         * audioQueLeadin1TimeCodeStart : 0:00.000
         * audioQueLeadin1TimeCodeEnd : 0:08.020
         * audioQueLeadin2TimeCodeStart : 0:08.020
         * audioQueLeadin2TimeCodeEnd : 0:34.743
         * audioQueLeadin3TimeCodeStart : 0:34.743
         * audioQueLeadin3TimeCodeEnd : 1:34.955
         */

        private int id;
        private String title;
        private String audioQueKeyLeadin;
        private String audioQueKeyLeadinFileName;
        private String audioQueLeadin1TimeCodeStart;
        private String audioQueLeadin1TimeCodeEnd;
        private String audioQueLeadin2TimeCodeStart;
        private String audioQueLeadin2TimeCodeEnd;
        private String audioQueLeadin3TimeCodeStart;
        private String audioQueLeadin3TimeCodeEnd;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAudioQueKeyLeadin() {
            return audioQueKeyLeadin;
        }

        public void setAudioQueKeyLeadin(String audioQueKeyLeadin) {
            this.audioQueKeyLeadin = audioQueKeyLeadin;
        }

        public String getAudioQueKeyLeadinFileName() {
            return audioQueKeyLeadinFileName;
        }

        public void setAudioQueKeyLeadinFileName(String audioQueKeyLeadinFileName) {
            this.audioQueKeyLeadinFileName = audioQueKeyLeadinFileName;
        }

        public String getAudioQueLeadin1TimeCodeStart() {
            return audioQueLeadin1TimeCodeStart;
        }

        public void setAudioQueLeadin1TimeCodeStart(String audioQueLeadin1TimeCodeStart) {
            this.audioQueLeadin1TimeCodeStart = audioQueLeadin1TimeCodeStart;
        }

        public String getAudioQueLeadin1TimeCodeEnd() {
            return audioQueLeadin1TimeCodeEnd;
        }

        public void setAudioQueLeadin1TimeCodeEnd(String audioQueLeadin1TimeCodeEnd) {
            this.audioQueLeadin1TimeCodeEnd = audioQueLeadin1TimeCodeEnd;
        }

        public String getAudioQueLeadin2TimeCodeStart() {
            return audioQueLeadin2TimeCodeStart;
        }

        public void setAudioQueLeadin2TimeCodeStart(String audioQueLeadin2TimeCodeStart) {
            this.audioQueLeadin2TimeCodeStart = audioQueLeadin2TimeCodeStart;
        }

        public String getAudioQueLeadin2TimeCodeEnd() {
            return audioQueLeadin2TimeCodeEnd;
        }

        public void setAudioQueLeadin2TimeCodeEnd(String audioQueLeadin2TimeCodeEnd) {
            this.audioQueLeadin2TimeCodeEnd = audioQueLeadin2TimeCodeEnd;
        }

        public String getAudioQueLeadin3TimeCodeStart() {
            return audioQueLeadin3TimeCodeStart;
        }

        public void setAudioQueLeadin3TimeCodeStart(String audioQueLeadin3TimeCodeStart) {
            this.audioQueLeadin3TimeCodeStart = audioQueLeadin3TimeCodeStart;
        }

        public String getAudioQueLeadin3TimeCodeEnd() {
            return audioQueLeadin3TimeCodeEnd;
        }

        public void setAudioQueLeadin3TimeCodeEnd(String audioQueLeadin3TimeCodeEnd) {
            this.audioQueLeadin3TimeCodeEnd = audioQueLeadin3TimeCodeEnd;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SCREENENGAGEBean {
        /**
         * title : Pimsleur Participar
         * hasIntro : 1
         * audioQueKeyIntroFileName : engage_intro.mp3
         */

        private String title;
        private int hasIntro;
        private String audioQueKeyIntroFileName;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getHasIntro() {
            return hasIntro;
        }

        public void setHasIntro(int hasIntro) {
            this.hasIntro = hasIntro;
        }

        public String getAudioQueKeyIntroFileName() {
            return audioQueKeyIntroFileName;
        }

        public void setAudioQueKeyIntroFileName(String audioQueKeyIntroFileName) {
            this.audioQueKeyIntroFileName = audioQueKeyIntroFileName;
        }
    }
}
