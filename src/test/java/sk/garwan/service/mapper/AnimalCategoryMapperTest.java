package sk.garwan.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnimalCategoryMapperTest {

    private AnimalCategoryMapper animalCategoryMapper;

    @BeforeEach
    public void setUp() {
        animalCategoryMapper = new AnimalCategoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(animalCategoryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(animalCategoryMapper.fromId(null)).isNull();
    }
}
