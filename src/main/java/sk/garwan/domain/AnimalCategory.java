package sk.garwan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sk.garwan.domain.enumeration.Category;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A AnimalCategory.
 */
@Entity
@Table(name = "animal_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnimalCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private Category name;

    @ManyToMany(mappedBy = "animalCategories")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
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

    public AnimalCategory name(Category name) {
        this.name = name;
        return this;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public AnimalCategory products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public AnimalCategory addProduct(Product product) {
        this.products.add(product);
        product.getAnimalCategories().add(this);
        return this;
    }

    public AnimalCategory removeProduct(Product product) {
        this.products.remove(product);
        product.getAnimalCategories().remove(this);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalCategory that = (AnimalCategory) o;
        return Objects.equals(id, that.id) &&
            name == that.name &&
            Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, products);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
