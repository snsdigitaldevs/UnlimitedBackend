package com.simonschuster.pimsleur.unlimited.services;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.CustomerInfoDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.AggregatedCustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Customer;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;
import com.simonschuster.pimsleur.unlimited.services.availableProducts.AvailableProductsService;
import com.simonschuster.pimsleur.unlimited.utils.DataConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
public class CustomerInfoConverterForAlexa {
    @Autowired
    private AvailableProductsService availableProductsService;

    public CustomerInfoDTO convertEDTModelToDto(AggregatedCustomerInfo customerInfos) throws IOException {
        CustomerInfo unlimitedCustomerInfo = customerInfos.getUnlimitedCustomerInfo();
        Customer puCustomer = unlimitedCustomerInfo.getResultData().getCustomer();
        SyncState pcmSyncState = customerInfos.getPcmSyncState();
        SyncState unlimitedSyncState = customerInfos.getUnlimitedSyncState();

        CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO(
                getPurchasedPUProductCodes(puCustomer, unlimitedCustomerInfo),
                getPurchasedPCMProductCodes(customerInfos.getPcmCustomerInfo()),
                unlimitedCustomerInfo.getResultData().getRegistrant().getProductActivations(),
                customerInfos.getProgressDTOS(),
                unlimitedCustomerInfo.getResultData().getRegistrant().getSubUsers());

        if (pcmSyncState.hasResultData()) {
            customerInfoDTO.setPcmLastSaveId(pcmSyncState.getResultData().getLastSaveId());
        }
        if (unlimitedSyncState.hasResultData()) {
            customerInfoDTO.setUnlimitedLastSaveId(unlimitedSyncState.getResultData().getLastSaveId());
        }
        customerInfoDTO.setCustomerId(puCustomer.getCustomersId().toString());
        customerInfoDTO.setHasPendingAndroid(puCustomer.hasPendingAndroid());
        customerInfoDTO.setHasPendingIos(puCustomer.hasPendingIos());
        customerInfoDTO.setRegistrantId(unlimitedCustomerInfo.getResultData().getRegistrant().getRegistrantId().toString());
        customerInfoDTO.setIdentityVerificationToken(puCustomer.getIdentityVerificationToken());
        return customerInfoDTO;
    }

    private List<String> getPurchasedPCMProductCodes(CustomerInfo pcmCustomerInfo) {

        if (pcmCustomerInfo.getResultData() != null) {
            return pcmCustomerInfo.getResultData().getCustomer().getAllOrdersProducts()
                    .stream()
                    .flatMap(ordersProduct -> availableProductsService.pcmOrderToDtos(ordersProduct))
                    .filter(productDto -> productDto.getLevel() != 0) // remove "how to learn"
                    .filter(DataConverterUtil.distinctByKey(AvailableProductDto::getProductCode)) // remove duplicate
                    .map(AvailableProductDto::getProductCode)
                    .collect(toList());
        } else {
            return emptyList();
        }
    }

    private List<String> getPurchasedPUProductCodes(Customer puCustomer, CustomerInfo unlimitedCustomerInfo) {
        if (unlimitedCustomerInfo.getResultData() != null) {
            return puCustomer.getAllOrdersProducts()
                    .stream()
                    .map(OrdersProduct::getProduct)
                    .flatMap(product -> availableProductsService.puProductToDtos(product))
                    .filter(DataConverterUtil.distinctByKey(p -> p.getLanguageName() + p.getLevel())) // remove duplicate
                    .map(AvailableProductDto::getProductCode)
                    .collect(toList());
        } else {
            return emptyList();
        }

    }


}
