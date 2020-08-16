package sk.garwan.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.garwan.domain.AnimalCategory;
import sk.garwan.service.dto.AnimalCategoryDTO;

/**
 * Mapper for the entity {@link AnimalCategory} and its DTO {@link AnimalCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnimalCategoryMapper extends EntityMapper<AnimalCategoryDTO, AnimalCategory> {


    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    AnimalCategory toEntity(AnimalCategoryDTO animalCategoryDTO);

    default AnimalCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        AnimalCategory animalCategory = new AnimalCategory();
        animalCategory.setId(id);
        return animalCategory;
    }
}
