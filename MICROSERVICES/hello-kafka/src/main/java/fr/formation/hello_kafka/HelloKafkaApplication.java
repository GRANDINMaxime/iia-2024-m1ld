package fr.formation.hello_kafka;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloKafkaApplication.class, args);
    }

    @Bean
    Consumer<String> onHelloEventConsumer() {
        return (evt) -> {
            System.out.println("Message reçu : " + evt);
        };
    }

    @Bean
    Function<String, String> onHelloUppercaseEventConsumer() {
        return (evt) -> {
            System.out.println("Message reçu à transformer : " + evt);

            return evt.toUpperCase();
        };
    }
}
