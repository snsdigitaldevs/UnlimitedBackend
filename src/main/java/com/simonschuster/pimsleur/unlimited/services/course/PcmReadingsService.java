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
                    findMatchingDownload(course, pcmProduct.getOrdersProducts());

            matchingDownload.ifPresent(ordersProductsDownload ->
                    course.setReadings(createReadingFrom(ordersProductsDownload, pcmProduct)));
        });
    }

    private Optional<OrdersProductsDownload> findMatchingDownload(Course course, List<OrdersProduct> ordersProducts) {
        return ordersProducts.stream()
                .flatMap(ordersProduct -> ordersProduct.getOrdersProductsAttributes().stream())
                .flatMap(ordersProductAttribute -> ordersProductAttribute.getOrdersProductsDownloads().stream())
                .filter(ordersProductsDownload -> Objects.equals(ordersProductsDownload.getMediaSet().getProduct().getProductCode(), course.getProductCode()))
                .findFirst();
    }

    private PcmReadings createReadingFrom(OrdersProductsDownload download, PcmProduct pcmProduct) {
        Stream<MediaItem> mediaItems = download.getMediaSet().getChildMediaSets().stream()
                .filter(ChildMediaSet::isReading)
                .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                .filter(MediaItem::isReading);

        boolean isBatched = parseBoolean(config.getProperty("toggle.fetch.mp3.url.batch"));
        if (isBatched) {
            return batchGetPcmReadings(download, pcmProduct, mediaItems);
        } else {
            return getPcmReadingsOneByOne(download, pcmProduct, mediaItems);
        }
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
                        mediaItem.getMediaItemIdMetadata());
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
                        mediaItem.getMediaItemIdMetadata());
                pcmReadings.getAudios().add(readingAudio);
            }
        });

        return pcmReadings;
    }
}
