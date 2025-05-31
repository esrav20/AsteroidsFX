package dk.sdu.cbse;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "dk.sdu.cbse")
public class AsteroidsFxApplication {

    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Set JavaFX to not exit on platform exit
        System.setProperty("javafx.application.auto.exit", "false");

        // Start Spring context
        springContext = SpringApplication.run(AsteroidsFxApplication.class, args);

        // Launch JavaFX application
        Application.launch(App.class, args);
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}