package com.simonschuster.pimsleur.unlimited.services.availableProducts;

import com.simonschuster.pimsleur.unlimited.data.dto.availableProducts.AvailableProductsDto;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Product;
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
import static java.util.stream.Stream.concat;

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

    public AvailableProductsDto getAvailableProducts(String sub) {
        if (sub == null) {
            return new AvailableProductsDto(emptyList(), getFreeProducts(emptyList()));
        }

        return CompletableFuture
                .supplyAsync(() -> purchasedPUAndPcmProducts(sub))
                .thenCombineAsync(
                        CompletableFuture.supplyAsync(() -> pcmFreeLessonsService.getPcmFreeLessons()),
                        this::getAvailableProductsDto)
                .join();
    }

    private AvailableProductsDto getAvailableProductsDto(List<AvailableProductDto> purchasedProducts, List<AvailableProductDto> pcmFreeLessons) {
        List<AvailableProductDto> freeProducts = getFreeProducts(purchasedProducts, pcmFreeLessons);
        return new AvailableProductsDto(purchasedProducts, freeProducts);
    }

    private List<AvailableProductDto> purchasedPUAndPcmProducts(String sub) {

        return CompletableFuture
                .supplyAsync(() -> getPuAvailableProducts(sub))
                .thenCombineAsync(
                        CompletableFuture.supplyAsync(() -> getPcmAvailableProducts(sub)),
                        this::getAvailableProductDtos)
                .join();

    }

    private List<AvailableProductDto> getAvailableProductDtos(List<AvailableProductDto> purchasedPuProducts, List<AvailableProductDto> purchasedPCMProducts) {
        Stream<AvailableProductDto> filteredPcmProducts = purchasedPCMProducts.stream()
                .filter((AvailableProductDto pcm) -> purchasedPuProducts.stream()
                        .noneMatch(pu -> pu.isSameLevelSameLang(pcm)));

        return concat(purchasedPuProducts.stream(), filteredPcmProducts)
                .sorted(comparing(AvailableProductDto::getCourseName))
                .collect(toList());
    }

    private List<AvailableProductDto> getPuAvailableProducts(String sub) {
        CustomerInfo puCustInfo = customerInfoService.getPUCustomerInfo(sub, "");
        if (puCustInfo.getResultData() != null) {
            List<AvailableProductDto> availableProductDto =
                    puCustInfo.getResultData().getCustomer().getAllOrdersProducts().stream()
                    .flatMap(order ->
                            puProductToDtos(order.getProduct())
                                    .peek(dto -> dto.setIsSubscription(order.isSubscription())))
                    .collect(Collectors.toList());

            List<AvailableProductDto> purchasedCourses = availableProductDto.stream()
                    .filter(p -> !HardCodedProductsUtil.puFreeIsbns.contains(p.getProductCode()))
                    .collect(Collectors.toList());

            List<AvailableProductDto> freePUDistinctCourses = availableProductDto.stream()
                    .filter(p -> HardCodedProductsUtil.puFreeIsbns.contains(p.getProductCode()))
                    .filter(puFreeCourse -> !freeCourseIsInPurchasedCourses(purchasedCourses, puFreeCourse))
                    .collect(Collectors.toList());

            purchasedCourses.addAll(freePUDistinctCourses);

            return purchasedCourses.stream()
                    .filter(distinctByKey(p -> p.getLanguageName() + p.getLevel())) // remove duplicate
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

    private List<AvailableProductDto> getPcmAvailableProducts(String sub) {
        CustomerInfo pcmCustInfo = customerInfoService.getPcmCustomerInfo(sub, "");

        if (pcmCustInfo.getResultData() != null) {
            return pcmCustInfo.getResultData()
                    .getCustomer().getCustomersOrders().stream()
                    .flatMap(customersOrder -> customersOrder.getOrdersProducts().stream()
                            .flatMap(order -> this.pcmOrderToDtos(order)
                                    .peek(dto -> dto.setIsSubscription(order.isSubscription()))))
                    .filter(dto -> dto.getLevel() != 0) // remove "how to learn"
                    .filter(distinctByKey(AvailableProductDto::getProductCode)) // remove duplicate
                    .collect(toList());
        } else {
            return emptyList();
        }
    }

    private List<AvailableProductDto> getFreeProducts(List<AvailableProductDto> purchasedProducts) {
        return getFreeProducts(purchasedProducts, pcmFreeLessonsService.getPcmFreeLessons());
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

    public Stream<AvailableProductDto> puProductToDtos(Product product) {
        List<Course> courses = puCourseInfoService.getPuProductInfo(product.getProductCode()).toDto();
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
