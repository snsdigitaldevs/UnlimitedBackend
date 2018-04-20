package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import java.util.List;

public class CourseLevelDef {
    private Integer isDemo;
    private String isbn13;
    private String fileListLocation;
    private String courseLangName;
    private String courseLevelName;
    private Integer mediaSetId;
    private String audioPath;
    private String mainLessonsThumbImagePath;
    private String mainLessonsFullImagePath;
    private String timeCodesDataPath;
    private String mediaSetDataFile;  //eg. 9781508243328_Mandarin_Chinese_1.json, where is this file? What is this file used for?
    private Integer requiredDiskSpace;
    private List<String> filesToIgnoreForExport;  //eg.9781442394872_Mandarin_Chinese1_Unit01_Reading.mp3
}
