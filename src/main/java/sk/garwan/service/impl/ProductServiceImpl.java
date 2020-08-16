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
import sk.garwan.service.mapper.ProductMapper;

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

    /**
     * Instantiates a new Product service.
     *
     * @param productRepository the product repository
     * @param productMapper     the product mapper
     */
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable)
            .map(productMapper::toDto);
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
    public Page<ProductDTO> findAllByPrice(Pageable pageable, Double priceMin, Double priceMax) {

        return productRepository.findAllByPriceIsGreaterThanAndPriceIsLessThan(priceMin, priceMax, pageable).map(productMapper::toDto);
    }

    @Override
    public Page<ProductDTO> findAllByName(Pageable pageable, String startsWith) {

        return productRepository.findAllByNameIsStartingWith(startsWith, pageable).map(productMapper::toDto);
    }
}
