package sk.garwan.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sk.garwan.GarwanApp;
import sk.garwan.domain.AnimalCategory;
import sk.garwan.domain.enumeration.Category;
import sk.garwan.repository.AnimalCategoryRepository;
import sk.garwan.service.AnimalCategoryService;
import sk.garwan.service.dto.AnimalCategoryDTO;
import sk.garwan.service.mapper.AnimalCategoryMapper;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AnimalCategoryResource} REST controller.
 */
@SpringBootTest(classes = GarwanApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnimalCategoryResourceIT {

    private static final Category DEFAULT_NAME = Category.DOG;
    private static final Category UPDATED_NAME = Category.CAT;

    @Autowired
    private AnimalCategoryRepository animalCategoryRepository;

    @Autowired
    private AnimalCategoryMapper animalCategoryMapper;

    @Autowired
    private AnimalCategoryService animalCategoryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnimalCategoryMockMvc;

    private AnimalCategory animalCategory;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalCategory createEntity(EntityManager em) {
        AnimalCategory animalCategory = new AnimalCategory()
            .name(DEFAULT_NAME);
        return animalCategory;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnimalCategory createUpdatedEntity(EntityManager em) {
        AnimalCategory animalCategory = new AnimalCategory()
            .name(UPDATED_NAME);
        return animalCategory;
    }

    @BeforeEach
    public void initTest() {
        animalCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnimalCategory() throws Exception {
        int databaseSizeBeforeCreate = animalCategoryRepository.findAll().size();
        // Create the AnimalCategory
        AnimalCategoryDTO animalCategoryDTO = animalCategoryMapper.toDto(animalCategory);
        restAnimalCategoryMockMvc.perform(post("/api/animal-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the AnimalCategory in the database
        List<AnimalCategory> animalCategoryList = animalCategoryRepository.findAll();
        assertThat(animalCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        AnimalCategory testAnimalCategory = animalCategoryList.get(animalCategoryList.size() - 1);
        assertThat(testAnimalCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAnimalCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = animalCategoryRepository.findAll().size();

        // Create the AnimalCategory with an existing ID
        animalCategory.setId(1L);
        AnimalCategoryDTO animalCategoryDTO = animalCategoryMapper.toDto(animalCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalCategoryMockMvc.perform(post("/api/animal-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalCategory in the database
        List<AnimalCategory> animalCategoryList = animalCategoryRepository.findAll();
        assertThat(animalCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalCategoryRepository.findAll().size();
        // set the field null
        animalCategory.setName(null);

        // Create the AnimalCategory, which fails.
        AnimalCategoryDTO animalCategoryDTO = animalCategoryMapper.toDto(animalCategory);


        restAnimalCategoryMockMvc.perform(post("/api/animal-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<AnimalCategory> animalCategoryList = animalCategoryRepository.findAll();
        assertThat(animalCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimalCategories() throws Exception {
        // Initialize the database
        animalCategoryRepository.saveAndFlush(animalCategory);

        // Get all the animalCategoryList
        restAnimalCategoryMockMvc.perform(get("/api/animal-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animalCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAnimalCategory() throws Exception {
        // Initialize the database
        animalCategoryRepository.saveAndFlush(animalCategory);

        // Get the animalCategory
        restAnimalCategoryMockMvc.perform(get("/api/animal-categories/{id}", animalCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(animalCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnimalCategory() throws Exception {
        // Get the animalCategory
        restAnimalCategoryMockMvc.perform(get("/api/animal-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimalCategory() throws Exception {
        // Initialize the database
        animalCategoryRepository.saveAndFlush(animalCategory);

        int databaseSizeBeforeUpdate = animalCategoryRepository.findAll().size();

        // Update the animalCategory
        AnimalCategory updatedAnimalCategory = animalCategoryRepository.findById(animalCategory.getId()).get();
        // Disconnect from session so that the updates on updatedAnimalCategory are not directly saved in db
        em.detach(updatedAnimalCategory);
        updatedAnimalCategory
            .name(UPDATED_NAME);
        AnimalCategoryDTO animalCategoryDTO = animalCategoryMapper.toDto(updatedAnimalCategory);

        restAnimalCategoryMockMvc.perform(put("/api/animal-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the AnimalCategory in the database
        List<AnimalCategory> animalCategoryList = animalCategoryRepository.findAll();
        assertThat(animalCategoryList).hasSize(databaseSizeBeforeUpdate);
        AnimalCategory testAnimalCategory = animalCategoryList.get(animalCategoryList.size() - 1);
        assertThat(testAnimalCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAnimalCategory() throws Exception {
        int databaseSizeBeforeUpdate = animalCategoryRepository.findAll().size();

        // Create the AnimalCategory
        AnimalCategoryDTO animalCategoryDTO = animalCategoryMapper.toDto(animalCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalCategoryMockMvc.perform(put("/api/animal-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(animalCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AnimalCategory in the database
        List<AnimalCategory> animalCategoryList = animalCategoryRepository.findAll();
        assertThat(animalCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnimalCategory() throws Exception {
        // Initialize the database
        animalCategoryRepository.saveAndFlush(animalCategory);

        int databaseSizeBeforeDelete = animalCategoryRepository.findAll().size();

        // Delete the animalCategory
        restAnimalCategoryMockMvc.perform(delete("/api/animal-categories/{id}", animalCategory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnimalCategory> animalCategoryList = animalCategoryRepository.findAll();
        assertThat(animalCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
