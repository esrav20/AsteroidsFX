package dk.sdu.cbse;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {
        "dk.sdu.cbse"   // all packages
})
public class AsteroidsFxApplication {

    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        System.setProperty("javafx.application.auto.exit", "false");

        springContext = SpringApplication.run(AsteroidsFxApplication.class, args);

        // Debug what Spring actually found
        System.out.println("=== DEBUG: Spring Bean Discovery ===");
        String[] allBeans = springContext.getBeanDefinitionNames();
        System.out.println("Total beans: " + allBeans.length);

        // Look for game-related beans
        for (String beanName : allBeans) {
            if (beanName.toLowerCase().contains("plugin") ||
                    beanName.toLowerCase().contains("control") ||
                    beanName.toLowerCase().contains("ast") ||
                    beanName.toLowerCase().contains("pew") ||
                    beanName.toLowerCase().contains("collision") ||
                    beanName.toLowerCase().contains("badguy") ||
                    beanName.toLowerCase().contains("goodguy")) {
                System.out.println("Found game bean: " + beanName + " -> " + springContext.getBean(beanName).getClass().getSimpleName());
            }
        }

        var gamePlugins = springContext.getBeansOfType(dk.sdu.cbse.common.services.IGamePluginService.class);
        var entityServices = springContext.getBeansOfType(dk.sdu.cbse.common.services.IEntityProcService.class);
        var postEntityServices = springContext.getBeansOfType(dk.sdu.cbse.common.services.IPostEntityProcService.class);

        System.out.println("IGamePluginService implementations: " + gamePlugins.size());
        System.out.println("IEntityProcService implementations: " + entityServices.size());
        System.out.println("IPostEntityProcService implementations: " + postEntityServices.size());
        System.out.println("=== END DEBUG ===");

        Application.launch(App.class, args);
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}