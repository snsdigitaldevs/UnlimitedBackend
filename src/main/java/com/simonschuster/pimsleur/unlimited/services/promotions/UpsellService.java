package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.simonschuster.pimsleur.unlimited.data.dto.promotions.PurchaseMapping;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.UpsellDto;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UpsellService {

    @Autowired
    private PurchaseMappingService purchaseMappingService;
    @Autowired
    private BundleIsbnService bundleIsbnService;
    @Autowired
    private EDTCustomerInfoService customerInfoService;

    public UpsellDto getUpsellInfoFor(String isbn, String sub) {

        PurchaseMapping purchaseMapping = purchaseMappingService.findPurchaseMappingFor(isbn);
        if (purchaseMapping == null) {
            // return empty result if can not find mapping for this isbn
            return new UpsellDto();
        } else {
            List<String> boughtIsbns = customerInfoService.getBoughtIsbns(sub);
            boolean upsellBought = isBought(boughtIsbns, purchaseMapping.getUpsellInAppPurchaseISBN());
            boolean upgradeBought = isBought(boughtIsbns, purchaseMapping.getUpgradeInAppPurchaseISBN());
            return purchaseMapping.toUpsellDto(upsellBought, upgradeBought);
        }
    }

    private boolean isBought(List<String> boughtIsbns, String isbn) {
        List<String> allFormatsOfIsbn = purchaseMappingService.getAllFormatsOf(isbn);

        boolean isIsbnBought = boughtIsbns.stream().anyMatch(allFormatsOfIsbn::contains);
        boolean isBundleBought = isBundleBought(boughtIsbns, allFormatsOfIsbn);

        return isIsbnBought || isBundleBought;

    }

    private boolean isBundleBought(List<String> boughtIsbns, List<String> allFormatsOfIsbn) {
        List<String> bundles = allFormatsOfIsbn.stream()
                .flatMap(oneFormat -> bundleIsbnService.getBundleIsbnsOf(oneFormat).stream())
                .distinct()
                .flatMap(bundle -> purchaseMappingService.getAllFormatsOf(bundle).stream())
                .collect(toList());

        return boughtIsbns.stream().anyMatch(bundles::contains);
    }

}
