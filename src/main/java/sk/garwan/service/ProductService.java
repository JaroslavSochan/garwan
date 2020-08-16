package sk.garwan.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sk.garwan.service.dto.ProductDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link sk.garwan.domain.Product}.
 */
public interface ProductService {

    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    ProductDTO save(ProductDTO productDTO);

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductDTO> findAll(Pageable pageable);

    /**
     * Get the "id" product.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductDTO> findOne(Long id);

    /**
     * Delete the "id" product.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<ProductDTO> findAllByPrice(Pageable pageable, Double priceMin, Double priceMax);

    Page<ProductDTO> findAllByName(Pageable pageable, String startsWith);
}
