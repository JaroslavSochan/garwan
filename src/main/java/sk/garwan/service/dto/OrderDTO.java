package sk.garwan.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Hashtable;

/**
 * A DTO for the {@link sk.garwan.domain.Order} entity.
 */
public class OrderDTO implements Serializable {

    private Long id;

    @NotNull
    private Double totalPrice;

    private ZonedDateTime createdAt;

    private Hashtable<Long, Integer> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Hashtable<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Hashtable<Long, Integer> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        return id != null && id.equals(((OrderDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", totalPrice=" + getTotalPrice() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
