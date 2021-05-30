package ru.eaptekahack.dreamteam.converter.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("NAME")
    private String name;
}