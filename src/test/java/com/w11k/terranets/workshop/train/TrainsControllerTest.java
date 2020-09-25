package com.w11k.terranets.workshop.train;

import com.w11k.terranets.workshop.types.Train;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@SpringBootTest
@Testcontainers
class TrainsControllerTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer("postgres:13");

    @Autowired
    TrainsController ctrl;

    private static final Logger log = LoggerFactory.getLogger(TrainsControllerTest.class);

    @DynamicPropertySource
    static void mySqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void getTrains() {
        Assertions.assertThat(ctrl).isNotNull();
        Train savedTrain = ctrl.addTrain(new Train("model", "Loco loco"));
        Train fromDb = ctrl.getTrainById(savedTrain.getId());
        Assertions.assertThat(savedTrain).isEqualTo(fromDb);
        log.info("logging from test");
    }

}
