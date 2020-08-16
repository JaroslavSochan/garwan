package sk.garwan.service.dto;

import sk.garwan.domain.enumeration.Category;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnimalCategoryDTO)) {
            return false;
        }

        return id != null && id.equals(((AnimalCategoryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
