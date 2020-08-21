package sk.garwan.service.dto;

import org.junit.jupiter.api.Test;
import sk.garwan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setId(1L);
        OrderDTO orderDTO2 = new OrderDTO();
        assertThat(orderDTO1).isNotEqualTo(orderDTO2);
        orderDTO2.setId(orderDTO1.getId());
        assertThat(orderDTO1).isEqualTo(orderDTO2);
        orderDTO2.setId(2L);
        assertThat(orderDTO1).isNotEqualTo(orderDTO2);
        orderDTO1.setId(null);
        assertThat(orderDTO1).isNotEqualTo(orderDTO2);
    }
}
