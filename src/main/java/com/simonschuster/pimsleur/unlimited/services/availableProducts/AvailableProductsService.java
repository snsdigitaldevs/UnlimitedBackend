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
import com.simonschuster.pimsleur.unlimited.utils.DataConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

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
        CustomerInfo puCustInfo = customerInfoService.getPUCustomerInfo(sub);
        if (puCustInfo.getResultData() != null) {
            return puCustInfo.getResultData().getCustomer().getAllOrdersProducts()
                    .stream()
                    .map(OrdersProduct::getProduct)
                    .flatMap(this::puProductToDtos)
                    .filter(DataConverterUtil.distinctByKey(p -> p.getLanguageName() + p.getLevel())) // remove duplicate
                    .collect(toList());
        } else {
            return emptyList();
        }
    }

    private List<AvailableProductDto> getPcmAvailableProducts(String sub) {
        CustomerInfo pcmCustInfo = customerInfoService.getPcmCustomerInfo(sub);

        pcmCustInfo.getResultData().getCustomer().getCustomersOrders().stream()
                .filter(order -> Objects.equals(order.getStoreDomain(), "pimsleur.com.mg2"));

        if (pcmCustInfo.getResultData() != null) {
            Optional<List<AvailableProductDto>> availableProductDtos = pcmCustInfo.getResultData().getCustomer()
                    .getCustomersOrders()
                    .stream()
                    .map(order -> {
                        Boolean isSubscriptionCourse = Objects.equals(order.getStoreDomain(), "pimsleur.com.mg2");

                        return order.getOrdersProducts().stream()
                                .flatMap(ordersProduct -> pcmOrderToDtos(ordersProduct, isSubscriptionCourse))
                                .filter(productDto -> productDto.getLevel() != 0) // remove "how to learn"
                                .filter(DataConverterUtil.distinctByKey(AvailableProductDto::getProductCode)) // remove duplicate
                                .collect(toList());
                    })
                    .reduce((result, availableProductDtoList) -> {
                        result.addAll(availableProductDtoList);
                        return result;
                    });
            return availableProductDtos.isPresent() ? availableProductDtos.get() : emptyList();
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
        boolean isKitted = courses.size() > 1;

        return courses.stream().map(course -> {
            AvailableProductDto dto = course.toPuAvailableProductDto();
            if (isKitted) {
                // if the pu product is kitted, use the mother isbn for upsell for all its children
                dto.setProductCodeForUpsell(product.getProductCode());
            } else {
                // otherwise, use the single level isbn for upsell api
                dto.setProductCodeForUpsell(product.getProductCode());
            }
            return dto;
        });
    }

    public Stream<AvailableProductDto> pcmOrderToDtos(OrdersProduct ordersProduct, Boolean isSubscriptionCourse) {
        List<AvailableProductDto> dtos = ordersProduct.getOrdersProductsAttributes().stream()
                .flatMap(attribute -> attribute.getOrdersProductsDownloads().stream())
                .map(download -> download.getMediaSet().getProduct())
                .map(product -> product.toPCMAvailableProductDto(isSubscriptionCourse))
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
