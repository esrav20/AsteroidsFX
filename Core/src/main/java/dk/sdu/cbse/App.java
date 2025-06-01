package dk.sdu.cbse;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameControls;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcService;
import dk.sdu.cbse.asteroid.Asteroid;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class App extends Application {
    private final VisualGameData vgData = new VisualGameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private Text scoreText;
    private int destroyedAsteroids = 0;

    // Spring will automatically inject all implementations
    private Collection<IGamePluginService> gamePlugins;
    private Collection<IEntityProcService> entityServices;
    private Collection<IPostEntityProcService> postEntityServices;

    @Override
    public void start(Stage window) throws Exception {
        // Get Spring context and load all services automatically
        ConfigurableApplicationContext context = AsteroidsFxApplication.getSpringContext();

        // Spring automatically finds all beans implementing these interfaces
        gamePlugins = context.getBeansOfType(IGamePluginService.class).values();
        entityServices = context.getBeansOfType(IEntityProcService.class).values();
        postEntityServices = context.getBeansOfType(IPostEntityProcService.class).values();

        System.out.println("Found " + gamePlugins.size() + " game plugins");
        System.out.println("Found " + entityServices.size() + " entity services");
        System.out.println("Found " + postEntityServices.size() + " post entity services");

        scoreText = new Text(10, 20, "Destroyed asteroids: 0");
        gameWindow.setPrefSize(vgData.getDisplayW(), vgData.getDisplayH());
        gameWindow.getChildren().add(scoreText);

        Scene scene = new Scene(gameWindow);
        setupKeyHandlers(scene);

        // Start all game plugins discovered by Spring
        for (IGamePluginService gamePlugin : gamePlugins) {
            System.out.println("Starting plugin: " + gamePlugin.getClass().getSimpleName());
            gamePlugin.start(vgData, world);
        }

        // Create initial polygons for existing entities
        for (Entity entity : world.getEntities()) {
            createPolygonForEntity(entity);
        }

        render();

        window.setScene(scene);
        window.setTitle("ASTEROIDS - Spring Framework");
        window.show();
    }

    private void setupKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                vgData.getControls().setControl(GameControls.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                vgData.getControls().setControl(GameControls.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                vgData.getControls().setControl(GameControls.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                vgData.getControls().setControl(GameControls.SPACE, true);
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                vgData.getControls().setControl(GameControls.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                vgData.getControls().setControl(GameControls.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                vgData.getControls().setControl(GameControls.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                vgData.getControls().setControl(GameControls.SPACE, false);
            }
        });
    }

    private void createPolygonForEntity(Entity entity) {
        if (!polygons.containsKey(entity)) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }
    }

    private void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                vgData.getControls().update();
            }
        }.start();
    }

    private void update() {
        // Track asteroid count before processing
        int asteroidsBefore = world.getEntities(Asteroid.class).size();

        // Process all entities using Spring-managed services
        for (IEntityProcService entityService : entityServices) {
            entityService.process(vgData, world);
        }

        // Create polygons for any new entities
        for (Entity entity : world.getEntities()) {
            createPolygonForEntity(entity);
        }

        // Post-processing (like collision detection) using Spring-managed services
        for (IPostEntityProcService postEntityService : postEntityServices) {
            postEntityService.process(vgData, world);
        }

        // Track asteroid count after processing
        int asteroidsAfter = world.getEntities(Asteroid.class).size();

        // If asteroids decreased, some were destroyed
        if (asteroidsBefore > asteroidsAfter) {
            destroyedAsteroids += (asteroidsBefore - asteroidsAfter);
            scoreText.setText("Destroyed asteroids: " + destroyedAsteroids);
        }
    }

    private void draw() {
        // Remove polygons for entities that no longer exist
        polygons.entrySet().removeIf(entry -> {
            Entity entity = entry.getKey();
            if (!world.getEntities().contains(entity)) {
                gameWindow.getChildren().remove(entry.getValue());
                return true;
            }
            return false;
        });

        // Update positions and rotations for all existing entities
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon != null) {
                polygon.setTranslateX(entity.getX());
                polygon.setTranslateY(entity.getY());
                polygon.setRotate(entity.getRotation());
            }
        }
    }
}