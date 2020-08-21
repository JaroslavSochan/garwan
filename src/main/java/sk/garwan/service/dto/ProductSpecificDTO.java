package sk.garwan.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link sk.garwan.domain.Product} entity.
 */
public class ProductSpecificDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Double price;

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
        ProductSpecificDTO that = (ProductSpecificDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(animalCategories, that.animalCategories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, animalCategories);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", animalCategories='" + getAnimalCategories() + "'" +
            "}";
    }
}
