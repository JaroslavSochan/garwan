package sk.garwan.service.dto;

import sk.garwan.domain.enumeration.Category;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sk.garwan.domain.AnimalCategory} entity.
 */
public class AnimalCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Category name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getName() {
        return name;
    }

    public void setName(Category name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalCategoryDTO that = (AnimalCategoryDTO) o;
        return Objects.equals(id, that.id) &&
            name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
