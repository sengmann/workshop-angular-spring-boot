package com.w11k.terranets.workshop;

import com.w11k.terranets.workshop.train.TrainRepository;
import com.w11k.terranets.workshop.types.Train;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(TrainRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Train("ICE", "ICE Locomotive")));
            log.info("Preloading " + repository.save(new Train("D-Zug", "Dampflok")));
        };
    }
}
