package sk.garwan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.garwan.domain.Product;

/**
 * Spring Data  repository for the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByPriceIsGreaterThanAndPriceIsLessThan(double priceMin, double priceMax, Pageable pageable);

    Page<Product> findAllByNameIsStartingWith(String startsWith, Pageable pageable);
}
