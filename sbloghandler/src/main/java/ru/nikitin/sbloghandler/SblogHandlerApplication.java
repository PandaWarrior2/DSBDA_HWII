package ru.nikitin.sbloghandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.nikitin.sbloghandler.properties.KafkaAppProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {KafkaAppProperties.class})
public class SblogHandlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SblogHandlerApplication.class, args);
    }

}
