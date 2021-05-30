package ru.eaptekahack.dreamteam.converter.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductOutputData {

    private Long id;

    private String name;

    private Double price;

    private String description;

    private String link;

    private String annotation;

    private boolean inStock;

    private String activeSubstance;

    private List<Analog> analogs = new ArrayList<>();


    // Данные самого дешевого из аналогов
    private String cheapestAnalogName;

    private Double cheapestAnalogPrice;

    private String cheapestAnalogDescription;

    private String cheapestAnalogLink;

    private String cheapestAnalogAnnotation;

}
