package sk.garwan.service.dto;

import org.junit.jupiter.api.Test;
import sk.garwan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class AnimalCategoryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnimalCategoryDTO.class);
        AnimalCategoryDTO animalCategoryDTO1 = new AnimalCategoryDTO();
        animalCategoryDTO1.setId(1L);
        AnimalCategoryDTO animalCategoryDTO2 = new AnimalCategoryDTO();
        assertThat(animalCategoryDTO1).isNotEqualTo(animalCategoryDTO2);
        animalCategoryDTO2.setId(animalCategoryDTO1.getId());
        assertThat(animalCategoryDTO1).isEqualTo(animalCategoryDTO2);
        animalCategoryDTO2.setId(2L);
        assertThat(animalCategoryDTO1).isNotEqualTo(animalCategoryDTO2);
        animalCategoryDTO1.setId(null);
        assertThat(animalCategoryDTO1).isNotEqualTo(animalCategoryDTO2);
    }
}
