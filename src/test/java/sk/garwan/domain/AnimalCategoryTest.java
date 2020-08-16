package sk.garwan.domain;

import org.junit.jupiter.api.Test;
import sk.garwan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class AnimalCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnimalCategory.class);
        AnimalCategory animalCategory1 = new AnimalCategory();
        animalCategory1.setId(1L);
        AnimalCategory animalCategory2 = new AnimalCategory();
        animalCategory2.setId(animalCategory1.getId());
        assertThat(animalCategory1).isEqualTo(animalCategory2);
        animalCategory2.setId(2L);
        assertThat(animalCategory1).isNotEqualTo(animalCategory2);
        animalCategory1.setId(null);
        assertThat(animalCategory1).isNotEqualTo(animalCategory2);
    }
}
