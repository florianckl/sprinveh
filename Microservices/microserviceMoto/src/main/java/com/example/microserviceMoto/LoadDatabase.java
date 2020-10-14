package com.example.microserviceMoto;



import com.example.microserviceMoto.model.Moto;
import com.example.microserviceMoto.repository.MotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(MotoRepository carRepository) {

        return args -> {
            carRepository.save(new Moto("ducati", "fgdfdf", 30000,"capture2.png"));
            carRepository.findAll().forEach(voiture -> {
                log.info("Preloaded " + voiture);
            });
        };
    }
}