package org.burnedpie.selena;

import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.dao.RadioStationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * Created by jibe on 22/08/16.
 */
@SpringBootApplication
public class SelenaApplication {

    private static final Logger log = LoggerFactory.getLogger(SelenaApplication.class);

    /**
     * Starts web server, for test purpose.
     * @param args
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SelenaApplication.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

    @Bean
    public CommandLineRunner demo(RadioStationRepository repository, ConfigurationRepository configurationRepository) {
        return (args) -> {
            // save a couple of customers
            repository.save(new RadioStation("Europe1", "http://test.url", 1));
            configurationRepository.save(new Configuration(ConfigurationKeyEnum.AIRPLAY_NAME, "test"));

            // fetch all customers
            log.info("Radio found with findAll():");
            log.info("-------------------------------");
            for (RadioStation station : repository.findAll()) {
                log.info(station.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            RadioStation station = repository.findOne(1L);
            log.info("Radio found with findOne(1L):");
            log.info("--------------------------------");
            log.info(station.toString());
            log.info("");

            // fetch customers by last name
            log.info("Radio found with findByChannel(1):");
            log.info("--------------------------------------------");
            RadioStation station1 = repository.findByChannel(1);
            log.info(station1.toString());
            log.info("");

            // fetch configuration airplay
            log.info("Configuratin found with findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME)");
            log.info("--------------------------------------------");
            Configuration configuration = configurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME);
            log.info(configuration.getConfigValue());
            log.info("");
        };
    }
}
