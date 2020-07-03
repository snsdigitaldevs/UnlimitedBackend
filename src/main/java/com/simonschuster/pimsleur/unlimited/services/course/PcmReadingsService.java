package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.ReadingAudio;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Readings;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.ChildMediaSet;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.MediaItem;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProductsDownload;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.BatchedMediaItemUrls;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.MediaItemUrl;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.PcmProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.simonschuster.pimsleur.unlimited.data.dto.productinfo.ReadingAudio.createFrom;
import static java.lang.Boolean.parseBoolean;

@Service
public class PcmReadingsService {
    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private PcmMediaItemUrlService pcmMediaItemUrlService;

    public void addReadingsToCourses(List<Course> courses, PcmProduct pcmProduct, String storeDomain) {
        courses.forEach(course -> {
            Optional<OrdersProductsDownload> matchingDownload =
                    findMatchingDownload(pcmProduct.getOrdersProducts(), course.getProductCode());
            matchingDownload.ifPresent(download ->
                    course.setReadings(createPcmReadings(download, pcmProduct, storeDomain)));
        });
    }

    private Optional<OrdersProductsDownload> findMatchingDownload(List<OrdersProduct> ordersProducts, String productCode) {
        return ordersProducts.stream()
                .flatMap(ordersProduct -> ordersProduct.getOrdersProductsAttributes().stream())
                .flatMap(attribute -> attribute.getOrdersProductsDownloads().stream())
                .filter(download -> Objects.equals(download.getMediaSet().getProduct().getProductCode(), productCode))
                .findFirst();
    }

    private Readings createPcmReadings(OrdersProductsDownload download, PcmProduct pcmProduct, String storeDomain) {
        Stream<MediaItem> mediaItems = findReadingMediaItems(download.getMediaSet().getChildMediaSets());

        boolean isBatched = parseBoolean(config.getProperty("toggle.fetch.mp3.url.batch"));
        Readings readings = isBatched ? batchGetPcmReadings(download, pcmProduct, mediaItems, storeDomain) : getPcmReadingsOneByOne(download, pcmProduct, mediaItems, storeDomain);

        List<ReadingAudio> audios = readings.getAudios();
        List<ReadingAudio> cultureNotesAudios = audios.stream().filter(ReadingAudio::isCultureNotes).collect(Collectors.toList());
        audios.removeAll(cultureNotesAudios);
        audios.addAll(cultureNotesAudios);
        readings.setAudios(audios);
        readings.setPdfName();

        return readings;
    }

    private Stream<MediaItem> findReadingMediaItems(List<ChildMediaSet> childMediaSets) {
        return childMediaSets.stream()
                .filter(ChildMediaSet::isReading)
                .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                .filter(MediaItem::isReading);
    }

    private Readings getPcmReadingsOneByOne(OrdersProductsDownload download, PcmProduct pcmProduct,
                                            Stream<MediaItem> mediaItems, String storeDomain) {
        Readings readings = new Readings();

        mediaItems.forEach(mediaItem -> {
            MediaItemUrl mediaItemUrl = pcmMediaItemUrlService.getMediaItemUrl(mediaItem.getMediaItemId(),
                    pcmProduct.getCustomerToken(), download.getEntitlementToken(), pcmProduct.getCustomersId(), storeDomain);
            if (mediaItem.isPdf()) {
                readings.setPdf(mediaItemUrl.getResult_data().getUrl());
            } else {
                ReadingAudio readingAudio = createFrom(
                        mediaItem.getMediaItemTitle(),
                        mediaItemUrl.getResult_data().getUrl(),
                        mediaItem.getMediaItemIdMetadata(),
                        mediaItem.getMediaItemId());
                readings.getAudios().add(readingAudio);
            }
        });

        return readings;
    }

    private Readings batchGetPcmReadings(OrdersProductsDownload download, PcmProduct pcmProduct, Stream<MediaItem> mediaItems, String storeDomain) {
        Readings readings = new Readings();

        BatchedMediaItemUrls batchedMediaItemUrls = pcmMediaItemUrlService.getBatchedMediaItemUrls(
                download.getMediaSetId(), pcmProduct.getCustomerToken(),
                download.getEntitlementToken(), pcmProduct.getCustomersId(), storeDomain);

        mediaItems.forEach(mediaItem -> {
            if (mediaItem.isPdf()) {
                readings.setPdf(batchedMediaItemUrls.getUrlOfMediaItem(mediaItem.getMediaItemFileNameWithoutExtension()));
            } else {
                ReadingAudio readingAudio = createFrom(
                        mediaItem.getMediaItemTitle(),
                        batchedMediaItemUrls.getUrlOfMediaItem(mediaItem.getMediaItemFileNameWithoutExtension()),
                        mediaItem.getMediaItemIdMetadata(),
                        mediaItem.getMediaItemId());
                readings.getAudios().add(readingAudio);
            }
        });

        return readings;
    }
}
