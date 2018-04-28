package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CourseLevelDef {
    @JsonProperty("isDemo")
    private Integer isDemo;
    @JsonProperty("isbn13")
    private String isbn13;
    @JsonProperty("fileListLocation")
    private String fileListLocation;
    @JsonProperty("courseLangName")
    private String courseLangName;
    @JsonProperty("courseLevelName")
    private String courseLevelName;
    @JsonProperty("mediaSetId")
    private Integer mediaSetId;
    @JsonProperty("audioPath")
    private String audioPath;
    @JsonProperty("mainLessonsThumbImagePath")
    private String mainLessonsThumbImagePath;
    @JsonProperty("mainLessonsFullImagePath")
    private String mainLessonsFullImagePath;
    @JsonProperty("timeCodesDataPath")
    private String timeCodesDataPath;
    @JsonProperty("mediaSetDataFile")
    private String mediaSetDataFile;  //eg. 9781508243328_Mandarin_Chinese_1.json, where is this file? What is this file used for?
    @JsonProperty("requiredDiskSpace")
    private Integer requiredDiskSpace;
    @JsonProperty("filesToIgnoreForExport")
    private List<String> filesToIgnoreForExport;  //eg.9781442394872_Mandarin_Chinese1_Unit01_Reading.mp3

    @JsonProperty("userPrefs")
    @JsonIgnoreProperties
    private UserPrefs userPrefs;

    @JsonProperty("appDefines")
    @JsonIgnoreProperties
    private AppDefinesOfCourseLevelDef appDefines;

    @JsonProperty
    private Object fileList;

    @JsonProperty
    private Object hasSeperateReadingLessonIntro;

    public Object getFileList() {
        return fileList;
    }

    public void setFileList(Object fileList) {
        this.fileList = fileList;
    }

    public Object getHasSeperateReadingLessonIntro() {
        return hasSeperateReadingLessonIntro;
    }

    public void setHasSeperateReadingLessonIntro(Object hasSeperateReadingLessonIntro) {
        this.hasSeperateReadingLessonIntro = hasSeperateReadingLessonIntro;
    }

    public Integer getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(Integer isDemo) {
        this.isDemo = isDemo;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getFileListLocation() {
        return fileListLocation;
    }

    public void setFileListLocation(String fileListLocation) {
        this.fileListLocation = fileListLocation;
    }

    public String getCourseLangName() {
        return courseLangName;
    }

    public void setCourseLangName(String courseLangName) {
        this.courseLangName = courseLangName;
    }

    public String getCourseLevelName() {
        return courseLevelName;
    }

    public void setCourseLevelName(String courseLevelName) {
        this.courseLevelName = courseLevelName;
    }

    public Integer getMediaSetId() {
        return mediaSetId;
    }

    public void setMediaSetId(Integer mediaSetId) {
        this.mediaSetId = mediaSetId;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getMainLessonsThumbImagePath() {
        return mainLessonsThumbImagePath;
    }

    public void setMainLessonsThumbImagePath(String mainLessonsThumbImagePath) {
        this.mainLessonsThumbImagePath = mainLessonsThumbImagePath;
    }

    public String getMainLessonsFullImagePath() {
        return mainLessonsFullImagePath;
    }

    public void setMainLessonsFullImagePath(String mainLessonsFullImagePath) {
        this.mainLessonsFullImagePath = mainLessonsFullImagePath;
    }

    public String getTimeCodesDataPath() {
        return timeCodesDataPath;
    }

    public void setTimeCodesDataPath(String timeCodesDataPath) {
        this.timeCodesDataPath = timeCodesDataPath;
    }

    public String getMediaSetDataFile() {
        return mediaSetDataFile;
    }

    public void setMediaSetDataFile(String mediaSetDataFile) {
        this.mediaSetDataFile = mediaSetDataFile;
    }

    public Integer getRequiredDiskSpace() {
        return requiredDiskSpace;
    }

    public void setRequiredDiskSpace(Integer requiredDiskSpace) {
        this.requiredDiskSpace = requiredDiskSpace;
    }

    public List<String> getFilesToIgnoreForExport() {
        return filesToIgnoreForExport;
    }

    public void setFilesToIgnoreForExport(List<String> filesToIgnoreForExport) {
        this.filesToIgnoreForExport = filesToIgnoreForExport;
    }

    public UserPrefs getUserPrefs() {
        return userPrefs;
    }

    public void setUserPrefs(UserPrefs userPrefs) {
        this.userPrefs = userPrefs;
    }

    public AppDefinesOfCourseLevelDef getAppDefines() {
        return appDefines;
    }

    public void setAppDefines(AppDefinesOfCourseLevelDef appDefines) {
        this.appDefines = appDefines;
    }
}
