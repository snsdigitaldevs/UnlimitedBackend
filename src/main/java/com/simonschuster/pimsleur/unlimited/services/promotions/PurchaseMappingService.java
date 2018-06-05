package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.UnlimitedApplication;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.BundleInfo;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.BundleToIndividuals;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.PurchaseMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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

    public PurchaseMapping getPurchaseMappingFor(String isbn) {
        return purchaseMappings.stream()
                .filter(x -> x.matches(isbn))
                .findFirst()
                .orElse(null);
    }

    private static List<PurchaseMapping> readJsonFile() throws IOException {
        InputStream fileStream = UnlimitedApplication.class.getClassLoader()
                .getResourceAsStream("isbn-mapping/purchase-mappings.json");

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileStream, new TypeReference<List<PurchaseMapping>>() {
        });
    }
}
