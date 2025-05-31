package dk.sdu.cbse;

import com.sun.tools.javac.Main;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameControls;
import dk.sdu.cbse.common.data.VisualGameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcService;
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

class App extends Application {
    private final VisualGameData vgData = new VisualGameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();


    public static void main(String[] args) {
        launch(App.class);
    }
    
    @Override
    public void start(Stage window) throws Exception {
        Text text = new Text(10, 20, "Destroyed asteroids: 0");
        gameWindow.setPrefSize(vgData.getDisplayW(), vgData.getDisplayH());
        gameWindow.getChildren().add(text);

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

            if (event.getCode().equals(KeyCode.SPACE)) {
                vgData.getControls().setControl(GameControls.SPACE, false);
            }

        });

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : getPluginServices()) {
            iGamePlugin.start(vgData, world);
        }
        for (Entity entity : world.getEntities()) {

            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        render();

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();

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

        // Update
        for (IEntityProcService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(vgData, world);
        }
        for (Entity entity : world.getEntities()) {
            if (polygons.get(entity) == null){
                Polygon polygon = new Polygon(entity.getPolygonCoordinates());
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
        }

        for (IPostEntityProcService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(vgData, world);
        }

    }

    private void draw() {

        for (Entity entityInPolygons : polygons.keySet()) {
            if(!world.getEntities().contains(entityInPolygons)){
                gameWindow.getChildren().remove(polygons.get(entityInPolygons));
                polygons.remove(entityInPolygons);
            }
        }



        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
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