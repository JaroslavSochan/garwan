package sk.garwan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.garwan.domain.Product;
import sk.garwan.repository.ProductRepository;
import sk.garwan.service.ProductService;
import sk.garwan.service.dto.ProductDTO;
import sk.garwan.service.dto.ProductDetailDTO;
import sk.garwan.service.dto.ProductSpecificDTO;
import sk.garwan.service.mapper.ProductDetailMapper;
import sk.garwan.service.mapper.ProductMapper;
import sk.garwan.service.mapper.ProductSpecificMapper;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;
    private final ProductDetailMapper productDetailMapper;

    private final ProductSpecificMapper productSpecificMapper;

    /**
     * Instantiates a new Product service.
     *
     * @param productRepository     the product repository
     * @param productMapper         the product mapper
     * @param productDetailMapper
     * @param productSpecificMapper
     */
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ProductDetailMapper productDetailMapper, ProductSpecificMapper productSpecificMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productDetailMapper = productDetailMapper;
        this.productSpecificMapper = productSpecificMapper;
    }

    @Override
    public ProductDetailDTO save(@Valid ProductDetailDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productDetailMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productDetailMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSpecificDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable)
            .map(productSpecificMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id)
            .map(productMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductSpecificDTO> findAllByPrice(Pageable pageable, Double priceMin, Double priceMax) {

        return productRepository.findAllByPriceIsGreaterThanAndPriceIsLessThan(priceMin, priceMax, pageable).map(productSpecificMapper::toDto);
    }

    @Override
    public Page<ProductSpecificDTO> findAllByName(Pageable pageable, String startsWith) {

        return productRepository.findAllByNameIsStartingWith(startsWith, pageable).map(productSpecificMapper::toDto);
    }
}
