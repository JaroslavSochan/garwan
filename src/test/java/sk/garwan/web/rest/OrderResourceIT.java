package sk.garwan.web.rest;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import sk.garwan.GarwanApp;
import sk.garwan.domain.Authority;
import sk.garwan.domain.Order;
import sk.garwan.domain.Product;
import sk.garwan.domain.User;
import sk.garwan.repository.OrderRepository;
import sk.garwan.repository.ProductRepository;
import sk.garwan.repository.UserRepository;
import sk.garwan.security.AuthoritiesConstants;
import sk.garwan.security.jwt.TokenProvider;
import sk.garwan.service.OrderService;
import sk.garwan.service.dto.OrderDTO;
import sk.garwan.service.mapper.OrderMapper;
import sk.garwan.web.rest.errors.ExceptionTranslator;
import sk.garwan.web.rest.vm.LoginVM;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sk.garwan.security.jwt.JWTFilter.AUTHORIZATION_HEADER;
import static sk.garwan.web.rest.TestUtil.createFormattingConversionService;

/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@SpringBootTest(classes = GarwanApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrderResourceIT {

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .createdAt(DEFAULT_CREATED_AT);
        return order;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdAt(UPDATED_CREATED_AT);
        return order;
    }

    public static User createLoggedUser(EntityManager em, String password) {
        User user = new User();
        user.setEmail("test-user@test.sk");
        user.setLogin("user");
        user.setPassword(password);
        user.setActivated(true);

        Set<Authority> authoritiesConstantsSet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authoritiesConstantsSet.add(authority);

        user.setAuthorities(authoritiesConstantsSet);
        return user;
    }

    public static User createLoggedAdmin(EntityManager em, String password) {
        User user = new User();
        user.setEmail("test@test.sk");
        user.setLogin("admin");
        user.setPassword(password);
        user.setActivated(true);

        Set<Authority> authoritiesConstantsSet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authoritiesConstantsSet.add(authority);

        user.setAuthorities(authoritiesConstantsSet);
        return user;
    }

    private String login() throws Exception {
        User loggedUser = createLoggedUser(em, passwordEncoder.encode("user"));
        if (!userRepository.existsByLogin(loggedUser.getLogin()))
            loggedUser = userRepository.saveAndFlush(loggedUser);

        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user");

        final UserJWTController userJWTController = new UserJWTController(tokenProvider, authenticationManagerBuilder);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userJWTController)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();

        MvcResult result = mockMvc.perform(post("/api/public/authenticate").contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(loginVM)).header("User-Agent", "unit-test"))
            .andExpect(status().isOk())
            .andReturn();

        String token = new JSONObject(result.getResponse().getContentAsString()).getString("id_token");
        return token;
    }

    private String loginAdmin() throws Exception {
        User loggedAdmin = createLoggedAdmin(em, passwordEncoder.encode("admin"));
        if (!userRepository.existsByLogin(loggedAdmin.getLogin()))
            loggedAdmin = userRepository.saveAndFlush(loggedAdmin);

        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("admin");
        loginVM.setPassword("admin");

        final UserJWTController userJWTController = new UserJWTController(tokenProvider, authenticationManagerBuilder);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userJWTController)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();

        MvcResult result = mockMvc.perform(post("/api/public/authenticate").contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(loginVM)).header("User-Agent", "unit-test"))
            .andExpect(status().isOk())
            .andReturn();

        String token = new JSONObject(result.getResponse().getContentAsString()).getString("id_token");
        return token;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order

        Product product = new Product();
        product.setName("test");
        product.setPrice(10.0);
        product.setDescription("Description");
        product = productRepository.saveAndFlush(product);

        User loggedUser = createLoggedUser(em, passwordEncoder.encode("user"));
        if (userRepository.existsByLogin(loggedUser.getLogin()))
            loggedUser = userRepository.findOneByLogin(loggedUser.getLogin()).get();

        Hashtable<Long, Integer> products = new Hashtable<>();
        products.put(product.getId(), 2);

        OrderDTO orderDTO = orderMapper.toDto(order);
        orderDTO.setUserId(loggedUser.getId());
        orderDTO.setProducts(products);
        restOrderMockMvc.perform(post("/api/public/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void createOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order with an existing ID
        order.setId(1L);
        OrderDTO orderDTO = orderMapper.toDto(order);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc.perform(post("/api/public/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc.perform(get("/api/private/orders?sort=id,desc").header(AUTHORIZATION_HEADER, "Bearer " + loginAdmin()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(order.getId().intValue())));
    }

    @Test
    @Transactional
    public void updateNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc.perform(put("/api/public/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc.perform(delete("/api/public/orders/{id}", order.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
