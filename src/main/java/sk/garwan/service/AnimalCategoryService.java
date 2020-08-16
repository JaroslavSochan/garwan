package sk.garwan.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sk.garwan.service.dto.AnimalCategoryDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link sk.garwan.domain.AnimalCategory}.
 */
public interface AnimalCategoryService {

    /**
     * Save a animalCategory.
     *
     * @param animalCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    AnimalCategoryDTO save(AnimalCategoryDTO animalCategoryDTO);

    /**
     * Get all the animalCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnimalCategoryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" animalCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnimalCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" animalCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
