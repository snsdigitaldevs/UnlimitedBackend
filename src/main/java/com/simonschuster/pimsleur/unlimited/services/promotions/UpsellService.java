package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.simonschuster.pimsleur.unlimited.aop.annotation.LogCostTime;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.FormatMapping;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.PurchaseMapping;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.UpsellDto;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UpsellService {

    @Autowired
    private PurchaseMappingService purchaseMappingService;
    @Autowired
    private BundleIsbnService bundleIsbnService;
    @Autowired
    private FormatMappingService formatMappingService;

    @Autowired
    private EDTCustomerInfoService customerInfoService;


    @LogCostTime
    public UpsellDto getUpsellInfoFor(String isbn, String sub, String email, String storeDomain) {
        PurchaseMapping purchaseMapping = purchaseMappingService.findPurchaseMappingFor(isbn);

        UpsellDto upsellDto = new UpsellDto();
        if (purchaseMapping == null) {
            // return empty result if can not find mapping for this isbn
            return upsellDto;
        } else {
            List<String> boughtIsbns = new ArrayList<>();
            if (!StringUtils.isEmpty(sub)) {
                boughtIsbns = customerInfoService.getBoughtIsbns(sub, email, storeDomain);
            }
            boolean upsellBought = isBought(boughtIsbns, purchaseMapping.getUpsellInAppPurchaseISBN());
            boolean subBought = isBought(boughtIsbns, purchaseMapping.getUpsell2InAppPurchaseISBN());
            boolean upgradeBought = isBought(boughtIsbns, purchaseMapping.getUpgradeInAppPurchaseISBN());

            upsellDto = purchaseMapping.toUpsellDto(upsellBought, subBought, upgradeBought);
            UpsellDto finalUpsellDto = formatMappingService.updateNameDescriptionLink(upsellDto);

            // find the item whose 'Other format ISBN' equals upsell ISBN
            FormatMapping withOtherFormatAs = formatMappingService.findISBNWithOtherFormatAs(
                    purchaseMapping.getUpsellInAppPurchaseISBN());
            if (withOtherFormatAs != null && finalUpsellDto.getNextLevel() != null) {
                finalUpsellDto.getNextLevel().setBaseISBN(withOtherFormatAs.getISBN());
            }
            return finalUpsellDto;
        }
    }

    private boolean isBought(List<String> boughtIsbns, String isbn) {
        List<String> allFormatsOfIsbn = formatMappingService.getAllFormatsOf(isbn);
        boolean isIsbnBought = boughtIsbns.stream().anyMatch(allFormatsOfIsbn::contains);
        boolean isBundleBought = isBundleBought(boughtIsbns, allFormatsOfIsbn);
        return isIsbnBought || isBundleBought;
    }

    private boolean isBundleBought(List<String> boughtIsbns, List<String> allFormatsOfIsbn) {
        List<String> bundles = allFormatsOfIsbn.stream()
                .flatMap(oneFormat -> bundleIsbnService.getBundleIsbnsOf(oneFormat).stream())
                .distinct()
                .flatMap(bundle -> formatMappingService.getAllFormatsOf(bundle).stream())
                .collect(toList());
        return boughtIsbns.stream().anyMatch(bundles::contains);
    }
}
