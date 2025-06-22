package net.demo.backendservice.dao;

import net.demo.backendservice.entities.Category;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");
    @Autowired
    CategoryRepository categoryRepository;

    List<Category> categories = new ArrayList<>();

    @BeforeEach
    void setUp() {
        System.out.println("-------------------------------------");
        this.categories = List.of(
                new Category(null,"test","test", new ArrayList<>()),
                new Category(null, "test 2", "test 2", new ArrayList<>())
        );
        categoryRepository.saveAll(this.categories);
        System.out.println("-------------------------------------");
    }

    @Test
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldFindCategoryByNameIgnoringTheCase() {
        // given
        String categoryName = "test";

        // when
        Optional<Category> category = categoryRepository.findByNameIgnoreCase(categoryName);

        // then
        assertThat(category).isPresent();
    }

    @Test
    void shouldNotFindCategoryByName() {
        // given
        String categoryName = "xxx";

        // when
        Optional<Category> category = categoryRepository.findByNameIgnoreCase(categoryName);

        // then
        assertThat(category).isEmpty();
    }
}