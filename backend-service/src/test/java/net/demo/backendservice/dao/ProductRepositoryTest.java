package net.demo.backendservice.dao;

import net.demo.backendservice.entities.Category;
import net.demo.backendservice.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ProductRepositoryTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    List<Product> products = new ArrayList<>();

    @BeforeEach
    void setUp() {
        System.out.println("-----------------------------------------------------------");
        Category category = new Category();
        category.setName("electronics");
        category.setDescription("Electronic devices");

        this.products = List.of(
                new Product(null,"Product 1","Desc 1",5550.0, category, new HashSet<>()),
                new Product(null, "Product 2","Desc 2",45200.0, category, new HashSet<>())
        );

        categoryRepository.save(category);
        productRepository.saveAll(products);
        System.out.println("------------------------------------------------------------");
    }

    @Test
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldFindProductByName() {
        // given
        String name = "Product 1";

        // when
        Optional<Product> product = productRepository.findProductByNameIgnoreCase(name);

        // then
        assertThat(product).isPresent();
    }

    @Test
    void shouldNotFindProductByName() {
        // given
        String name = "www";

        // when
        Optional<Product> product = productRepository.findProductByNameIgnoreCase(name);

        // then
        assertThat(product).isEmpty();
    }
}