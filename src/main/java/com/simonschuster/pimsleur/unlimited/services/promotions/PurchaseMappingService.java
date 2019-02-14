package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.UnlimitedApplication;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.PurchaseMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

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

    public PurchaseMapping findISBNWithOtherFormatAs(String isbn) {
        Stream<PurchaseMapping> purchaseMappingStream = purchaseMappings.stream().filter(x -> x.getOtherFormat1ISBN().equals(isbn));
        return purchaseMappingStream.findFirst().orElse(null);
    }

    public List<String> getAllFormatsOf(String isbn) {
        PurchaseMapping mapping = findPurchaseMappingFor(isbn);
        if (mapping == null) {
            return singletonList(isbn);
        } else {
            return mapping.getAllFormats();
        }
    }

    private static List<PurchaseMapping> readJsonFile() throws IOException {
        InputStream fileStream = UnlimitedApplication.class.getClassLoader()
                .getResourceAsStream("isbn-mapping/purchase-mappings.json");

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileStream, new TypeReference<List<PurchaseMapping>>() {
        });
    }
}
