package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.UnlimitedApplication;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.IsbnNameDescription;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.UpsellDto;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.UpsellItem;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;

@Service
public class IsbnNameDescriptionService {

    private static List<IsbnNameDescription> namesAndDescription;

    static {
        try {
            //only do this once per runtime
            namesAndDescription = readJsonFile();
        } catch (IOException ignored) {
        }
    }

    public UpsellDto updateNameDescription(UpsellDto upsellDto) {
        of(upsellDto.getNextLevel(), upsellDto.getNextVersion(), upsellDto.getNextSubscription())
                .forEach(this::updateNameDescriptionForUpsellItem);
        return upsellDto;
    }

    private void updateNameDescriptionForUpsellItem(UpsellItem upsellItem) {
        if (upsellItem != null) {
            IsbnNameDescription find = findFormatMappingFor(upsellItem.getIsbn());
            if(find != null){
                upsellItem.setName(find.getInAppDisplayName());
                upsellItem.setDescription(find.getInAppDescription());
            }
        }
    }

    private static List<IsbnNameDescription> readJsonFile() throws IOException {
        InputStream fileStream = UnlimitedApplication.class.getClassLoader()
                .getResourceAsStream("isbn-mapping/format-mapping.json");

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileStream, new TypeReference<List<IsbnNameDescription>>() {
        });
    }

    public IsbnNameDescription findFormatMappingFor(String isbn) {
        return namesAndDescription.stream()
                .filter(x -> x.matches(isbn))
                .findFirst()
                .orElse(null);
    }

    public IsbnNameDescription findISBNWithOtherFormatAs(String isbn) {
        Stream<IsbnNameDescription> formatMappingStream = namesAndDescription.stream().filter(
                x -> x.getOtherFormat1ISBN().equals(isbn) ||
                x.getOtherFormat2ISBN().equals(isbn) ||
                x.getOtherFormat3ISBN().equals(isbn));
        return formatMappingStream.findFirst().orElse(null);
    }
}
