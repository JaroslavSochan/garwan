package sk.garwan.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sk.garwan.security.AuthoritiesConstants;
import sk.garwan.service.AnimalCategoryService;
import sk.garwan.service.dto.AnimalCategoryDTO;
import sk.garwan.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link sk.garwan.domain.AnimalCategory}.
 */
@RestController
@RequestMapping("/api/private")
public class AnimalCategoryResource {

    private static final String ENTITY_NAME = "animalCategory";
    private final Logger log = LoggerFactory.getLogger(AnimalCategoryResource.class);
    private final AnimalCategoryService animalCategoryService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public AnimalCategoryResource(AnimalCategoryService animalCategoryService) {
        this.animalCategoryService = animalCategoryService;
    }

    /**
     * {@code POST  /animal-categories} : Create a new animalCategory.
     *
     * @param animalCategoryDTO the animalCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new animalCategoryDTO, or with status {@code 400 (Bad Request)} if the animalCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @PostMapping("/animal-categories")
    public ResponseEntity<AnimalCategoryDTO> createAnimalCategory(@Valid @RequestBody AnimalCategoryDTO animalCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save AnimalCategory : {}", animalCategoryDTO);
        if (animalCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new animalCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnimalCategoryDTO result = animalCategoryService.save(animalCategoryDTO);
        return ResponseEntity.created(new URI("/api/animal-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /animal-categories} : Updates an existing animalCategory.
     *
     * @param animalCategoryDTO the animalCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated animalCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the animalCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the animalCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @PutMapping("/animal-categories")
    public ResponseEntity<AnimalCategoryDTO> updateAnimalCategory(@Valid @RequestBody AnimalCategoryDTO animalCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update AnimalCategory : {}", animalCategoryDTO);
        if (animalCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnimalCategoryDTO result = animalCategoryService.save(animalCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, animalCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /animal-categories} : get all the animalCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of animalCategories in body.
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @GetMapping("/animal-categories")
    public ResponseEntity<Page<AnimalCategoryDTO>> getAllAnimalCategories(Pageable pageable) {
        log.debug("REST request to get a page of AnimalCategories");
        Page<AnimalCategoryDTO> page = animalCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    /**
     * {@code GET  /animal-categories/:id} : get the "id" animalCategory.
     *
     * @param id the id of the animalCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the animalCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @GetMapping("/animal-categories/{id}")
    public ResponseEntity<AnimalCategoryDTO> getAnimalCategory(@PathVariable Long id) {
        log.debug("REST request to get AnimalCategory : {}", id);
        Optional<AnimalCategoryDTO> animalCategoryDTO = animalCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(animalCategoryDTO);
    }

    /**
     * {@code DELETE  /animal-categories/:id} : delete the "id" animalCategory.
     *
     * @param id the id of the animalCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @DeleteMapping("/animal-categories/{id}")
    public ResponseEntity<Void> deleteAnimalCategory(@PathVariable Long id) {
        log.debug("REST request to delete AnimalCategory : {}", id);
        animalCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
