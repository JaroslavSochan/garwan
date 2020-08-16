package sk.garwan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.garwan.domain.AnimalCategory;

/**
 * Spring Data  repository for the AnimalCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnimalCategoryRepository extends JpaRepository<AnimalCategory, Long> {
}
