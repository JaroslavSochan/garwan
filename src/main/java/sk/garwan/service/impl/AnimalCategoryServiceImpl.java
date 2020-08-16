package sk.garwan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.garwan.domain.AnimalCategory;
import sk.garwan.repository.AnimalCategoryRepository;
import sk.garwan.service.AnimalCategoryService;
import sk.garwan.service.dto.AnimalCategoryDTO;
import sk.garwan.service.mapper.AnimalCategoryMapper;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AnimalCategory}.
 */
@Service
@Transactional
public class AnimalCategoryServiceImpl implements AnimalCategoryService {

    private final Logger log = LoggerFactory.getLogger(AnimalCategoryServiceImpl.class);

    private final AnimalCategoryRepository animalCategoryRepository;

    private final AnimalCategoryMapper animalCategoryMapper;

    public AnimalCategoryServiceImpl(AnimalCategoryRepository animalCategoryRepository, AnimalCategoryMapper animalCategoryMapper) {
        this.animalCategoryRepository = animalCategoryRepository;
        this.animalCategoryMapper = animalCategoryMapper;
    }

    @Override
    public AnimalCategoryDTO save(AnimalCategoryDTO animalCategoryDTO) {
        log.debug("Request to save AnimalCategory : {}", animalCategoryDTO);
        AnimalCategory animalCategory = animalCategoryMapper.toEntity(animalCategoryDTO);
        animalCategory = animalCategoryRepository.save(animalCategory);
        return animalCategoryMapper.toDto(animalCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnimalCategories");
        return animalCategoryRepository.findAll(pageable)
            .map(animalCategoryMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnimalCategoryDTO> findOne(Long id) {
        log.debug("Request to get AnimalCategory : {}", id);
        return animalCategoryRepository.findById(id)
            .map(animalCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnimalCategory : {}", id);
        animalCategoryRepository.deleteById(id);
    }
}
