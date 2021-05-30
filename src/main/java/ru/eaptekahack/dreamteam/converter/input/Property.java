package ru.eaptekahack.dreamteam.converter.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Property {

    @JsonProperty("IBLOCK_ELEMENT_ID")
    private Long elementId;

    @JsonProperty("PROPERTY_356")
    private String description1;

    @JsonProperty("PROPERTY_567")
    private String description2;
}
