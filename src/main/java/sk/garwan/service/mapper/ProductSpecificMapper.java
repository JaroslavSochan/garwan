package sk.garwan.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.garwan.domain.Product;
import sk.garwan.service.dto.ProductDTO;
import sk.garwan.service.dto.ProductSpecificDTO;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrderMapper.class, AnimalCategoryMapper.class})
public interface ProductSpecificMapper extends EntityMapper<ProductSpecificDTO, Product> {


    @Mapping(target = "removeOrder", ignore = true)
    @Mapping(target = "removeAnimalCategory", ignore = true)

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
