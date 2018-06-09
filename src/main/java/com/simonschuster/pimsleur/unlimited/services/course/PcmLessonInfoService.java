package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Boolean.parseBoolean;
import static java.util.stream.Collectors.toList;

@Service
public class PcmLessonInfoService {
    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private PcmMediaItemUrlService pcmMediaItemUrlService;
    @Autowired
    private PcmMediaItemsFilterService pcmMediaItemsFilterService;

    public List<Course> getPcmLessonInfo(String productCode, PcmProduct pcmProduct) {
        List<MediaSetByLevel> entitlementTokens = new ArrayList<>();
        List<MediaItemsByLevel> matchedMediaItems = pcmMediaItemsFilterService.getMatchedMediaItems(pcmProduct, entitlementTokens, productCode);

        PcmAudioReqParams pcmAudioReqParams = new PcmAudioReqParams();
        pcmAudioReqParams.setMediaItemIds(matchedMediaItems);
        pcmAudioReqParams.setEntitlementTokens(entitlementTokens);
        pcmAudioReqParams.setCustomersId(pcmProduct.getCustomersId());
        pcmAudioReqParams.setCustomerToken(pcmProduct.getCustomerToken());

        return getLessonsInfo(pcmAudioReqParams);
    }

    private List<Course> getLessonsInfo(PcmAudioReqParams params) {
        List<Course> coursesWithLessonInfoOnly = new ArrayList<>();

        boolean isBatched = parseBoolean(config.getProperty("toggle.fetch.mp3.url.batch"));
        params.getMediaItemIds().forEach(mediaItemsByLevel -> {
            Course course = new Course();
            course.setLevel(Integer.valueOf(mediaItemsByLevel.getLevel()));
            if (isBatched) {
                course.setLessons(batchFetchLessons(params, mediaItemsByLevel));

            } else {
                course.setLessons(fetchLessonsOneByOne(params, mediaItemsByLevel));
            }
            coursesWithLessonInfoOnly.add(course);
        });

        return coursesWithLessonInfoOnly;
    }

    private List<Lesson> batchFetchLessons(PcmAudioReqParams pcmAudioReqParams, MediaItemsByLevel mediaItemsByLevel) {
        String level = mediaItemsByLevel.getLevel();

        Integer mediaSetId = getMatchedMediaSetId(pcmAudioReqParams, level);
        String entitlementToken = getMatchedEntitlementToken(pcmAudioReqParams, level);

        BatchedMediaItemUrls batchedMediaItemUrls =
                pcmMediaItemUrlService.getBatchedMediaItemUrls(mediaSetId,
                        pcmAudioReqParams.getCustomerToken(),
                        entitlementToken,
                        pcmAudioReqParams.getCustomersId());

        return mediaItemsByLevel.getMediaItems().stream().map(mediaItem -> {
            Lesson lesson = new Lesson();
            String title = mediaItem.getMediaItemTitle();
            Integer itemId = mediaItem.getMediaItemId();

            String fileName = title
                    .replace("Lesson", "Unit")
                    .replace("Lección", "Unit")
                    .replace(" ", "_");
            String lessonNumber = title.split(" ")[1];
            fileName = lessonNumber.length() == 1 ? fileName.replace(lessonNumber, "0" + lessonNumber) : fileName;

            String urlOfMediaItem = batchedMediaItemUrls.getUrlOfMediaItem(fileName);
            lesson.setAudioLink(urlOfMediaItem);

            lesson.setName(title);
            lesson.setLevel(Integer.parseInt(level));
            lesson.setMediaItemId(itemId);
            lesson.setLessonNumber(title.split(" ")[1]);
            return lesson;
        }).collect(toList());

    }

    private Integer getMatchedMediaSetId(PcmAudioReqParams pcmAudioReqParams, String level) {
        Optional<Integer> first = pcmAudioReqParams.getEntitlementTokens().stream()
                .filter(mediaSetByLevel -> mediaSetByLevel.getLevel().equals(level))
                .map(MediaSetByLevel::getMediaSetId)
                .findFirst();

        return first.isPresent() ? first.get() : 0;
    }

    private String getMatchedEntitlementToken(PcmAudioReqParams pcmAudioReqParams, String level) {
        Optional<String> entitlementToken = pcmAudioReqParams.getEntitlementTokens().stream()
                .filter(mediaSetByLevel -> mediaSetByLevel.getLevel().equals(level))
                .map(MediaSetByLevel::getEntitlementToken)
                .findFirst();
        return entitlementToken.isPresent() ? entitlementToken.get() : StringUtils.EMPTY;
    }

    private List<Lesson> fetchLessonsOneByOne(PcmAudioReqParams pcmAudioReqParams, MediaItemsByLevel mediaItemsByLevel) {

        String level = mediaItemsByLevel.getLevel();

        return mediaItemsByLevel.getMediaItems().parallelStream().map(mediaItem -> {

            String title = mediaItem.getMediaItemTitle();
            Integer mediaItemId = mediaItem.getMediaItemId();
            String entitlementToken = getMatchedEntitlementToken(pcmAudioReqParams, level);

            MediaItemUrl mediaItemUrl = pcmMediaItemUrlService.getMediaItemUrl(mediaItemId,
                    pcmAudioReqParams.getCustomerToken(),
                    entitlementToken,
                    pcmAudioReqParams.getCustomersId());

            Lesson lesson = new Lesson();
            lesson.setAudioLink(mediaItemUrl.getResult_data().getUrl());
            lesson.setName(title);
            lesson.setLevel(Integer.parseInt(level));
            lesson.setMediaItemId(mediaItemId);
            lesson.setLessonNumber(title.split(" ")[1]);
            return lesson;
        }).collect(toList());
    }
}
