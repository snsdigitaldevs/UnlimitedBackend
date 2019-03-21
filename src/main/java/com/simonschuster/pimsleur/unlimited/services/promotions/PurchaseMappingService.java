package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.UnlimitedApplication;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.PurchaseMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class PurchaseMappingService {

    private static List<PurchaseMapping> purchaseMappings;

    static {
        try {
            //only do this once per runtime
            purchaseMappings = readJsonFile();
        } catch (IOException ignored) {
        }
    }

    public PurchaseMapping findPurchaseMappingFor(String isbn) {
        return purchaseMappings.stream()
                .filter(x -> x.matches(isbn))
                .findFirst()
                .orElse(null);
    }

    private static List<PurchaseMapping> readJsonFile() throws IOException {
        InputStream fileStream = UnlimitedApplication.class.getClassLoader()
                .getResourceAsStream("isbn-mapping/purchase-mapping.json");

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileStream, new TypeReference<List<PurchaseMapping>>() {
        });
    }
}
