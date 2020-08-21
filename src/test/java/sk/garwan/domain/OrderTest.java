package sk.garwan.domain;

import org.junit.jupiter.api.Test;
import sk.garwan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    public void equalsVerifier() throws Exception {
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);
        order2.setId(2L);
        assertThat(order1).isNotEqualTo(order2);
        order1.setId(null);
        assertThat(order1).isNotEqualTo(order2);
    }
}
