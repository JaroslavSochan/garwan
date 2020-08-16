package sk.garwan.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.garwan.domain.Order;
import sk.garwan.service.dto.OrderDTO;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {


    @Mapping(target = "products", ignore = true)
    OrderDTO toDto(Order order);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    default Order fromId(Long id) {
        if (id == null) {
            return null;
        }
        Order order = new Order();
        order.setId(id);
        return order;
    }
}
