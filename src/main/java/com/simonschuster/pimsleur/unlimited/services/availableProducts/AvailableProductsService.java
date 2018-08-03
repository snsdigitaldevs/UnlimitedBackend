package com.simonschuster.pimsleur.unlimited.services.availableProducts;

import com.simonschuster.pimsleur.unlimited.data.dto.availableProducts.AvailableProductsDto;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Product;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.ResultData;
import com.simonschuster.pimsleur.unlimited.services.course.PUCourseInfoService;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.PcmFreeLessonsService;
import com.simonschuster.pimsleur.unlimited.services.freeLessons.PuFreeLessonsService;
import com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.simonschuster.pimsleur.unlimited.utils.DataConverterUtil.distinctByKey;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class AvailableProductsService {
    @Autowired
    private EDTCustomerInfoService customerInfoService;
    @Autowired
    private PcmFreeLessonsService pcmFreeLessonsService;
    @Autowired
    private PuFreeLessonsService puFreeLessonsService;
    @Autowired
    private PUCourseInfoService puCourseInfoService;

    public AvailableProductsDto getAvailableProducts(String sub, String email, String storeDomain) {
        if (sub == null) {
            return new AvailableProductsDto(emptyList(), getFreeProducts(emptyList(), storeDomain));
        }

        return CompletableFuture
                .supplyAsync(() -> purchasedPUAndPcmProducts(sub, email, storeDomain))
                .thenCombineAsync(
                        CompletableFuture.supplyAsync(() -> pcmFreeLessonsService.getPcmFreeLessons(storeDomain)),
                        this::getAvailableProductsDto)
                .join();
    }

    private AvailableProductsDto getAvailableProductsDto(List<AvailableProductDto> purchasedProducts, List<AvailableProductDto> pcmFreeLessons) {
        List<AvailableProductDto> freeProducts = getFreeProducts(purchasedProducts, pcmFreeLessons);
        return new AvailableProductsDto(purchasedProducts, freeProducts);
    }

    private List<AvailableProductDto> purchasedPUAndPcmProducts(String sub, String email, String storeDomain) {
        List<AvailableProductDto> join = getAllAvailableProducts(sub, email, storeDomain);
        return join;
    }

    private List<AvailableProductDto> getAllAvailableProducts(String sub, String email, String storeDomain)
    {
        CustomerInfo customerInfo = customerInfoService.getPuAndPCMCustomerInfos(sub, storeDomain, email);
        ResultData resultData = customerInfo.getResultData();
        if (resultData != null) {
            List<AvailableProductDto> availableProductDto =
                    resultData.getCustomer().getAllOrdersProducts().stream()
                            .flatMap(order ->
                            {
                                boolean puProduct = order.isPUProduct();
                                Stream<AvailableProductDto> availableProductDtoStream =
                                        puProduct ?
                                        puProductToDtos(order.getProduct(), storeDomain) :
                                        pcmOrderToDtos(order);
                                return availableProductDtoStream
                                        .peek(dto -> dto.setIsSubscription(order.isSubscription()));
                            })
                            .filter(dto -> dto.getLevel() != 0)
                            .filter(distinctByKey(AvailableProductDto::getProductCode))
                            .collect(Collectors.toList());

            List<String> freeIsbns = HardCodedProductsUtil.puFreeIsbns;
            List<AvailableProductDto> purchasedCourses = availableProductDto.stream()
                    .filter(p -> !freeIsbns.contains(p.getProductCode()))
                    .collect(Collectors.toList());

            List<AvailableProductDto> purchasedFreePUDistinctCourses = availableProductDto.stream()
                    .filter(p -> freeIsbns.contains(p.getProductCode()))
                    .filter(puFreeCourse -> !freeCourseIsInPurchasedCourses(purchasedCourses, puFreeCourse))
                    .collect(Collectors.toList());

            purchasedCourses.addAll(purchasedFreePUDistinctCourses);

            return purchasedCourses.stream()
                    .sorted(comparing(AvailableProductDto::getCourseName))
                    .collect(toList());
        } else {
            return emptyList();
        }
    }

    private boolean freeCourseIsInPurchasedCourses(List<AvailableProductDto> purchasedCourses, AvailableProductDto puFreeCourse) {
        return purchasedCourses.stream().anyMatch(purchasedCourse ->
                (purchasedCourse.getLanguageName() + purchasedCourse.getLevel())
                        .equals(puFreeCourse.getLanguageName() + purchasedCourse.getLevel()));
    }

    private List<AvailableProductDto> getFreeProducts(List<AvailableProductDto> purchasedProducts, String storeDomain) {
        return getFreeProducts(purchasedProducts, pcmFreeLessonsService.getPcmFreeLessons(storeDomain));
    }

    private List<AvailableProductDto> getFreeProducts(List<AvailableProductDto> purchasedProducts,
                                                      List<AvailableProductDto> pcmFreeLessons) {
        List<AvailableProductDto> freeProducts = puFreeLessonsService
                .mergePuWithPcmFreeLessons(pcmFreeLessons);
        return freeProducts.stream()
                .filter(free -> purchasedProducts.stream().noneMatch(purchased -> purchased.isSameLang(free)))
                //for free lessons, the isbn used to call upsell api is always the original isbn itself
                .peek(free -> free.setProductCodeForUpsell(free.getProductCode()))
                .collect(toList());
    }

    public Stream<AvailableProductDto> puProductToDtos(Product product, String storeDomain) {
        List<Course> courses = puCourseInfoService.getPuProductInfo(product.getProductCode(), storeDomain).toDto();
        boolean hasMother = courses.size() > 1;

        return courses.stream().map(course -> {
            AvailableProductDto dto = course.toPuAvailableProductDto();
            if (hasMother) {
                // if the pu product is kitted or subscription, use the mother isbn for upsell for all its children
                dto.setProductCodeForUpsell(product.getProductCode());
            } else {
                // otherwise, use the single level isbn for upsell api
                dto.setProductCodeForUpsell(product.getProductCode());
            }
            return dto;
        });
    }

    public Stream<AvailableProductDto> pcmOrderToDtos(OrdersProduct ordersProduct) {
        List<AvailableProductDto> dtos = ordersProduct.getOrdersProductsAttributes().stream()
                .flatMap(attribute -> attribute.getOrdersProductsDownloads().stream())
                .map(download -> download.getMediaSet().getProduct())
                .map(Product::toPCMAvailableProductDto)
                .collect(toList());
        if (dtos.size() > 1) {
            // if it's subscription or kitted, use the mother isbn for upsell
            dtos.forEach(dto -> dto.setProductCodeForUpsell(ordersProduct.getProduct().getProductCode()));
        } else {
            // otherwise, use the single level isbn
            dtos.forEach(dto -> dto.setProductCodeForUpsell(dto.getProductCode()));
        }
        return dtos.stream();
    }
}
