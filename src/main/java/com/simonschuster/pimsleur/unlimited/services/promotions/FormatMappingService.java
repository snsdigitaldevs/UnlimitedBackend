package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.UnlimitedApplication;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.FormatMapping;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.UpsellDto;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.UpsellItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.stream.Stream.of;

@Service
public class FormatMappingService {

    private static List<FormatMapping> formatMappings;

    @Autowired
    private ApplicationConfiguration config;

    static {
        try {
            //only do this once per runtime
            formatMappings = readJsonFile();
        } catch (IOException ignored) {
        }
    }

    public UpsellDto updateNameDescriptionLink(UpsellDto upsellDto) {
        of(upsellDto.getNextLevel(), upsellDto.getNextVersion(), upsellDto.getNextSubscription())
                .forEach(this::updateNameDescriptionForUpsellItem);
        return updateWebCartLink(upsellDto);
    }

    private void updateNameDescriptionForUpsellItem(UpsellItem upsellItem) {
        if (upsellItem != null) {
            FormatMapping find = findFormatMappingFor(upsellItem.getIsbn());
            if(find != null){
                upsellItem.setName(find.getCourseName());
                upsellItem.setDescription(find.getCourseDescription());
            }
        }
    }

    private UpsellDto updateWebCartLink(UpsellDto upsellDto) {
        updateWebCartLinkForItem(upsellDto.getNextLevel(), config.getProperty("cart.api.purchase"));
        updateWebCartLinkForItem(upsellDto.getNextSubscription(), config.getProperty("cart.api.subscription"));
        updateWebCartLinkForItem(upsellDto.getNextVersion(), config.getProperty("cart.api.purchase"));
        return upsellDto;
    }

    private void updateWebCartLinkForItem(UpsellItem upsellItem, String link) {
        if (upsellItem != null && StringUtils.isNotEmpty(upsellItem.getWebLink())) {
            upsellItem.setPid(upsellItem.getWebLink());
            upsellItem.setWebLink(format(link, upsellItem.getWebLink()));
        }
    }

    private static List<FormatMapping> readJsonFile() throws IOException {
        InputStream fileStream = UnlimitedApplication.class.getClassLoader()
                .getResourceAsStream("isbn-mapping/format-mapping.json");

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileStream, new TypeReference<List<FormatMapping>>() {
        });
    }

    public List<String> getAllFormatsOf(String isbn) {
        FormatMapping mapping = findFormatMappingFor(isbn);
        if (mapping == null) {
            return singletonList(isbn);
        } else {
            return mapping.getAllFormats();
        }
    }

    public FormatMapping findFormatMappingFor(String isbn) {
        return formatMappings.stream()
                .filter(x -> x.matches(isbn))
                .findFirst()
                .orElse(null);
    }

    public FormatMapping findISBNWithOtherFormatAs(String isbn) {
        Stream<FormatMapping> formatMappingStream = formatMappings.stream().filter(
                x -> x.getOtherFormat1ISBN().equals(isbn) ||
                x.getOtherFormat2ISBN().equals(isbn) ||
                x.getOtherFormat3ISBN().equals(isbn));
        return formatMappingStream.findFirst().orElse(null);
    }
}
