package org.baljaguk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(exclude = RabbitAutoConfiguration.class)
public class GmjdApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmjdApplication.class, args);
    }

}
