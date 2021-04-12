package com.simonschuster.pimsleur.unlimited.data.edt.customer.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Image;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.LanguageImageMetadata;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.LanguageName;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;

public class Utils {
    private Utils() {
    }
    
    private static final String PCM_IMAGE_DOMAIN = "https://public.pimsleur.cdn.edtnet.us";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(ALLOW_SINGLE_QUOTES, true);

    public static Image GetImageFromLanguageMetaData(LanguageName languageName) {
        Image image = new Image();

        if (languageName != null && languageName.getLanguageImageMetadata() != null) {
            try {
                LanguageImageMetadata metadata = OBJECT_MAPPER.readValue(languageName.getLanguageImageMetadata(),
                    new TypeReference<LanguageImageMetadata>() {
                    });
                String imageUrl = PCM_IMAGE_DOMAIN + metadata.getImageFilePath() + metadata.getImageFileName();

                image.setThumbImageAddress(imageUrl);
                image.setFullImageAddress(imageUrl);
                image.setCredits(metadata.getCredits());
                return image;
            } catch (IOException ignored) {
                return image;
            }
        }

        return image;
    }
}
