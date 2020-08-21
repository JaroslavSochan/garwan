package sk.garwan.service.mapper;


import org.mapstruct.Mapper;
import sk.garwan.domain.Product;
import sk.garwan.service.dto.ProductDTO;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrderMapper.class, AnimalCategoryMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
