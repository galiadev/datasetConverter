package ru.eaptekahack.dreamteam.converter.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Basket {

    @JsonProperty("ORDER_ID")
    private Long orderId;

    @JsonProperty("PRODUCT_ID")
    private Long productId;

    @JsonProperty("QANTITY")
    private Double quantity;

    @JsonProperty("PRICE")
    private Double price;

    @JsonProperty("DETAIL_PAGE_URL")
    private String url;

    public int compareTo(Basket o) {
        return this.productId.compareTo(o.productId);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Basket basket = (Basket) o;
        return Objects.equals(productId, basket.productId);
    }

    public int hashCode() {
        return Objects.hash(productId);
    }
}
