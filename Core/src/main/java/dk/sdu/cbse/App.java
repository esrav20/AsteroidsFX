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

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

public class App extends Application {
    private final VisualGameData vgData = new VisualGameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private Text scoreText;
    private int destroyedAsteroids = 0;
    private int previousAsteroidCount = 0;

    // Public no-argument constructor (required for JavaFX Application)
    public App() {
        super();
    }

    public static void main(String[] args) {
        launch(App.class, args);
    }

    @Override
    public void start(Stage window) throws Exception {
        scoreText = new Text(10, 20, "Destroyed asteroids: 0");
        gameWindow.setPrefSize(vgData.getDisplayW(), vgData.getDisplayH());
        gameWindow.getChildren().add(scoreText);

        Scene scene = new Scene(gameWindow);
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

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : getPluginServices()) {
            iGamePlugin.start(vgData, world);
        }

        // Track initial asteroid count
        previousAsteroidCount = world.getEntities(Asteroid.class).size();

        // Create initial polygons for existing entities
        for (Entity entity : world.getEntities()) {
            createPolygonForEntity(entity);
        }

        render();

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    private void createPolygonForEntity(Entity entity) {
        if (!polygons.containsKey(entity)) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
            System.out.println("Created polygon for entity: " + entity.getClass().getSimpleName());
        }
    }

    private void render() {
        new AnimationTimer() {
            private long then = 0;

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

        // Process all entities
        for (IEntityProcService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(vgData, world);
        }

        // Create polygons for any new entities that were added during processing
        for (Entity entity : world.getEntities()) {
            createPolygonForEntity(entity);
        }

        // Post-processing (like collision detection)
        for (IPostEntityProcService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(vgData, world);
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
        for (Entity entityInPolygons : polygons.keySet()) {
            if (!world.getEntities().contains(entityInPolygons)) {
                Polygon polygon = polygons.get(entityInPolygons);
                gameWindow.getChildren().remove(polygon);
                polygons.remove(entityInPolygons);
                System.out.println("Removed polygon for entity: " + entityInPolygons.getClass().getSimpleName());
            }
        }

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

    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}