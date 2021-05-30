package ru.eaptekahack.dreamteam.converter.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalogActiveSubstance {

    @JsonProperty("MNN_ID")
    private String mnnId;

    @JsonProperty("PRODUCT_ID")
    private Long productId;

    @JsonProperty("MNN_NAME")
    private String activeSubstanceName;

    @JsonProperty("MNN_CODE")
    private String activeSubstanceCode;

    private Double price;

    private String name;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalogActiveSubstance activeSubstance = (AnalogActiveSubstance) o;
        return Objects.equals(productId, activeSubstance.productId);
    }

    public int hashCode() {
        return Objects.hash(productId);
    }

    private void copyParameters(AnalogActiveSubstance v) {
        this.mnnId = v.mnnId;
        this.productId = v.getProductId();
        this.activeSubstanceName = v.activeSubstanceName;
        this.activeSubstanceCode = v.getActiveSubstanceCode();
    }

    public void copyParametersAndSetPrice(AnalogActiveSubstance v, Map<Long, Basket> basketMap) {
        copyParameters(v);
        Double price = null;
        if(basketMap.containsKey(v.getProductId()))
            price = basketMap.get(v.getProductId()).getPrice();
        this.setPrice(price);
    }
}
