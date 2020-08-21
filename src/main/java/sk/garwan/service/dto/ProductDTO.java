package sk.garwan.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link sk.garwan.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Double price;

    private String description;

    private String gallery;

    private Set<OrderDTO> orders = new HashSet<>();

    private Set<AnimalCategoryDTO> animalCategories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }

    public Set<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderDTO> orders) {
        this.orders = orders;
    }

    public Set<AnimalCategoryDTO> getAnimalCategories() {
        return animalCategories;
    }

    public void setAnimalCategories(Set<AnimalCategoryDTO> animalCategories) {
        this.animalCategories = animalCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(description, that.description) &&
            Objects.equals(gallery, that.gallery) &&
            Objects.equals(orders, that.orders) &&
            Objects.equals(animalCategories, that.animalCategories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, gallery, orders, animalCategories);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            ", gallery='" + getGallery() + "'" +
            ", orders='" + getOrders() + "'" +
            ", animalCategories='" + getAnimalCategories() + "'" +
            "}";
    }
}
