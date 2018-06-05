package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.UnlimitedApplication;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.BundleInfo;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.BundleToIndividuals;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BundleIsbnService {

    private static List<BundleToIndividuals> bundleToIndividuals;

    static {
        try {
            bundleToIndividuals = initBundleToIndividuals();
        } catch (IOException ignored) {
        }
    }

    // this will tell you which bundle isbn includes the given isbn
    public List<String> getBundleIsbnsOf(String isbn) {
        return bundleToIndividuals.stream()
                .filter(bundle -> bundle.getChildIsbns().contains(isbn))
                .map(BundleToIndividuals::getBundleIsbn)
                .collect(toList());
    }

    private static List<BundleToIndividuals> initBundleToIndividuals() throws IOException {
        List<BundleToIndividuals> bundleToIndividuals = new ArrayList<>();

        List<BundleInfo> bundleInfos = readJsonFile();
        bundleInfos.forEach(bundleInfo -> {
            if (bundleInfo.isBundle()) {
                bundleToIndividuals.add(new BundleToIndividuals(bundleInfo.getBundleISBN(), new ArrayList<>()));
            } else {
                bundleToIndividuals.get(bundleToIndividuals.size() - 1)
                        .getChildIsbns().add(bundleInfo.get30LessonISBN());
            }
        });

        return bundleToIndividuals;
    }

    private static List<BundleInfo> readJsonFile() throws IOException {
        InputStream fileStream = UnlimitedApplication.class.getClassLoader()
                .getResourceAsStream("isbn-mapping/bundle-to-individuals.json");

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileStream, new TypeReference<List<BundleInfo>>() {
        });
    }
}
