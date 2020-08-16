package sk.garwan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.garwan.domain.Order;
import sk.garwan.domain.Product;
import sk.garwan.repository.OrderRepository;
import sk.garwan.repository.ProductRepository;
import sk.garwan.service.OrderService;
import sk.garwan.service.dto.OrderDTO;
import sk.garwan.service.mapper.OrderMapper;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return orderRepository.findAll(pageable)
            .map(orderMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id)
            .map(orderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {

        Order order = new Order();
        Set<Product> productList = new HashSet<>();

        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);

        orderDTO.getProducts().keySet().forEach(p -> {
            Product product = productRepository.findById(p).get();
            productList.add(product);
            totalPrice.updateAndGet(v -> v + product.getPrice() * orderDTO.getProducts().get(p));
        });

        order.setProducts(productList);
        order.setTotalPrice(totalPrice.get());
        order = orderRepository.saveAndFlush(order);
        return orderMapper.toDto(order);
    }
}
