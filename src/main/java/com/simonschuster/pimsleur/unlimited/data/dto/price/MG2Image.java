package com.simonschuster.pimsleur.unlimited.data.dto.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MG2Image {
    @JsonProperty("ImageId")
    private Integer imageId;

    @JsonProperty("ImagePath")
    private String imagePath;

    @JsonProperty("ImageDescription")
    private String imageDescription;

}
