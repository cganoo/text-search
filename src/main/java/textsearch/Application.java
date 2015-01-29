package textsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * The Application itself. Contains a java-default entry point
 * Created by cganoo on 28/01/15.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"textsearch.lucene", "textsearch.api", "textsearch.cli", "textsearch.utils"})
public class Application {

    /* Application entry point */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
