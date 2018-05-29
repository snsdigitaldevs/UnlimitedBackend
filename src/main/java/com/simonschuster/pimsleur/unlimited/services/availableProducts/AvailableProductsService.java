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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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

        List<AvailableProductDto> purchasedProducts = purchasedPUAndPcmProducts(sub);
        List<AvailableProductDto> freeProducts = getFreeProducts(purchasedProducts);

        return new AvailableProductsDto(purchasedProducts, freeProducts);
    }

    private List<AvailableProductDto> purchasedPUAndPcmProducts(String sub) {
        List<AvailableProductDto> purchasedPuProducts = getPuAvailableProducts(sub);
        List<AvailableProductDto> purchasedPCMProducts = getPcmAvailableProducts(sub);

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
                    .flatMap(product -> puCourseInfoService.getPuProductInfo(product.getProductCode()).toDto().stream())
                    .map(Course::toAvailableProductDto)
                    .collect(toList());
        } else {
            return emptyList();
        }
    }

    private List<AvailableProductDto> getPcmAvailableProducts(String sub) {
        CustomerInfo pcmCustInfo = customerInfoService.getPcmCustomerInfo(sub);

        if (pcmCustInfo.getResultData() != null) {
            return pcmCustInfo.getResultData().getCustomer().getAllOrdersProducts()
                    .stream()
                    .flatMap(ordersProduct -> ordersProduct.getOrdersProductsAttributes().stream())
                    .flatMap(attribute -> attribute.getOrdersProductsDownloads().stream())
                    .map(download -> download.getMediaSet().getProduct())
                    .map(Product::toPCMAvailableProductDto)
                    .filter(productDto -> productDto.getLevel() != 0) // remove "how to learn"
                    .filter(distinctByKey(p -> p.getLanguageName() + p.getLevel())) // remove duplicate
                    .collect(toList());
        } else {
            return emptyList();
        }
    }

    private List<AvailableProductDto> getFreeProducts(List<AvailableProductDto> purchasedProducts) {
        List<AvailableProductDto> freeProducts = puFreeLessonsService
                .mergePuWithPcmFreeLessons(pcmFreeLessonsService.getPcmFreeLessons());
        return freeProducts.stream()
                .filter(free -> purchasedProducts.stream().noneMatch(purchased -> purchased.isSameLang(free)))
                .collect(toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }
}
