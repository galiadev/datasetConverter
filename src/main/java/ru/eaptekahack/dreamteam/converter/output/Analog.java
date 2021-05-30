package ru.eaptekahack.dreamteam.converter.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.eaptekahack.dreamteam.converter.input.AnalogActiveSubstance;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Analog {

    private Long id;

    private String name;

    private Double price;

    private String activeSubstanceName;

    private String activeSubstanceCode;

    public static List<Analog> convert(List<AnalogActiveSubstance> analogs) {
        List<Analog> analogList = new ArrayList<>();
        analogs.forEach(item -> {
            Analog analog = new Analog();
            analog.setId(item.getProductId());
            analog.setName(item.getName());
            analog.setPrice(item.getPrice());
            analog.setActiveSubstanceCode(item.getActiveSubstanceCode());
            analog.setActiveSubstanceName(item.getActiveSubstanceName());
            analogList.add(analog);
        });

        return analogList;
    }
}
