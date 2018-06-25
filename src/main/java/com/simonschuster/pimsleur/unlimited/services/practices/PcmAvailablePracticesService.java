package com.simonschuster.pimsleur.unlimited.services.practices;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.*;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

@Service
public class PcmAvailablePracticesService {
    private static final String READING_LESSONS = "Reading Lessons";
    private static final String READING_LESSON = "Reading Lesson";
    private static final String CULTURE_NOTES = "Culture Notes";

    @Autowired
    private EDTCustomerInfoService edtCustomerInfoService;

    public AvailablePractices getAvailablePractices(String productCode, String sub) {
        CustomerInfo pcmCustomerInfo = edtCustomerInfoService.getPcmCustomerInfo(sub, "");

        if (pcmCustomerInfo.getResultData() != null) {
            return collectPractices(productCode, pcmCustomerInfo);
        }

        return null;
    }

    private AvailablePractices collectPractices(String productCode, CustomerInfo pcmCustomerInfo) {
        Stream<ChildMediaSet> readingLessons = findReadingLessons(productCode, pcmCustomerInfo);
        Stream<Integer> unitNumbers = unitNumbersOfReadingLessons(readingLessons);

        List<PracticesInUnit> practicesInUnits = unitNumbers
                .map(PracticesInUnit::onlyReadingAvailable)
                .collect(toList());

        return new AvailablePractices(practicesInUnits);
    }

    private Stream<Integer> unitNumbersOfReadingLessons(Stream<ChildMediaSet> readingLessons) {
        return readingLessons
                .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                .map(MediaItem::getMediaItemTitle)
                .map(this::titleToUnitNumber)
                .filter(unit -> unit != MIN_VALUE);
    }

    private Stream<ChildMediaSet> findReadingLessons(String productCode, CustomerInfo pcmCustomerInfo) {
        Stream<OrdersProduct> allOrdersProducts = pcmCustomerInfo.getResultData().getCustomer()
                .getCustomersOrders().stream()
                .flatMap(order -> order.getOrdersProducts().stream());

        return allOrdersProducts
                .flatMap(ordersProduct -> ordersProduct.getOrdersProductsAttributes().stream())
                .flatMap(ordersProductAttribute -> ordersProductAttribute.getOrdersProductsDownloads().stream())
                .map(OrdersProductsDownload::getMediaSet)
                .filter(mediaSet ->
                        productCode.equals(mediaSet.getProduct().getProductCode())
                )
                .flatMap(mediaSet -> mediaSet.getChildMediaSets().stream())
                .filter(this::isReadingLesson);
    }

    private boolean isReadingLesson(ChildMediaSet childMediaSet) {
        String title = childMediaSet.getMediaSetTitle();
        return title.contains(READING_LESSONS) || title.contains(CULTURE_NOTES);
    }

    private Integer titleToUnitNumber(String title) {
        String unit = title
                .replace(READING_LESSON, "")
                .replace(CULTURE_NOTES, "")
                .replace(" ", "");
        try {
            return parseInt(unit);
        } catch (NumberFormatException e) {
            return MIN_VALUE;
        }
    }
}
