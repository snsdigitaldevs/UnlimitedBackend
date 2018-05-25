package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.PcmReadingAudio;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.PcmReadings;
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
import java.util.stream.Stream;

import static com.simonschuster.pimsleur.unlimited.data.dto.productinfo.PcmReadingAudio.createFrom;
import static java.lang.Boolean.parseBoolean;

@Service
public class PcmReadingsService {
    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private PcmMediaItemUrlService pcmMediaItemUrlService;

    public void addReadingsToCourses(List<Course> courses, PcmProduct pcmProduct) {
        courses.forEach(course -> {
            Optional<OrdersProductsDownload> matchingDownload =
                    findMatchingDownload(pcmProduct.getOrdersProducts(), course.getProductCode());
            matchingDownload.ifPresent(download ->
                    course.setReadings(createPcmReadings(download, pcmProduct)));
        });
    }

    private Optional<OrdersProductsDownload> findMatchingDownload(List<OrdersProduct> ordersProducts, String productCode) {
        return ordersProducts.stream()
                .flatMap(ordersProduct -> ordersProduct.getOrdersProductsAttributes().stream())
                .flatMap(attribute -> attribute.getOrdersProductsDownloads().stream())
                .filter(download -> Objects.equals(download.getMediaSet().getProduct().getProductCode(), productCode))
                .findFirst();
    }

    private PcmReadings createPcmReadings(OrdersProductsDownload download, PcmProduct pcmProduct) {
        Stream<MediaItem> mediaItems = findReadingMediaItems(download.getMediaSet().getChildMediaSets());

        boolean isBatched = parseBoolean(config.getProperty("toggle.fetch.mp3.url.batch"));
        if (isBatched) {
            return batchGetPcmReadings(download, pcmProduct, mediaItems);
        } else {
            return getPcmReadingsOneByOne(download, pcmProduct, mediaItems);
        }
    }

    private Stream<MediaItem> findReadingMediaItems(List<ChildMediaSet> childMediaSets) {
        return childMediaSets.stream()
                .filter(ChildMediaSet::isReading)
                .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                .filter(MediaItem::isReading);
    }

    private PcmReadings getPcmReadingsOneByOne(OrdersProductsDownload download, PcmProduct pcmProduct,
                                               Stream<MediaItem> mediaItems) {
        PcmReadings pcmReadings = new PcmReadings();

        mediaItems.forEach(mediaItem -> {
            MediaItemUrl mediaItemUrl = pcmMediaItemUrlService.getMediaItemUrl(mediaItem.getMediaItemId(),
                    pcmProduct.getCustomerToken(), download.getEntitlementToken(), pcmProduct.getCustomersId());
            if (mediaItem.isPdf()) {
                pcmReadings.setPdf(mediaItemUrl.getResult_data().getUrl());
            } else {
                PcmReadingAudio readingAudio = createFrom(
                        mediaItem.getMediaItemTitle(),
                        mediaItemUrl.getResult_data().getUrl(),
                        mediaItem.getMediaItemIdMetadata(),
                        mediaItem.getMediaItemId());
                pcmReadings.getAudios().add(readingAudio);
            }
        });

        return pcmReadings;
    }

    private PcmReadings batchGetPcmReadings(OrdersProductsDownload download, PcmProduct pcmProduct, Stream<MediaItem> mediaItems) {
        PcmReadings pcmReadings = new PcmReadings();

        BatchedMediaItemUrls batchedMediaItemUrls = pcmMediaItemUrlService.getBatchedMediaItemUrls(
                download.getMediaSetId(), pcmProduct.getCustomerToken(),
                download.getEntitlementToken(), pcmProduct.getCustomersId());

        mediaItems.forEach(mediaItem -> {
            if (mediaItem.isPdf()) {
                pcmReadings.setPdf(batchedMediaItemUrls.getUrlOfMediaItem(mediaItem.getMediaItemId()));
            } else {
                PcmReadingAudio readingAudio = createFrom(
                        mediaItem.getMediaItemTitle(),
                        batchedMediaItemUrls.getUrlOfMediaItem(mediaItem.getMediaItemId()),
                        mediaItem.getMediaItemIdMetadata(),
                        mediaItem.getMediaItemId());
                pcmReadings.getAudios().add(readingAudio);
            }
        });

        return pcmReadings;
    }
}
