package com.tira;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
@Log4j2
public class MapApplication extends Application {
    public void start(Stage stage) throws IOException {
        OctileHeader octileHeader = new OctileHeader();

        IHeuristic iHeuristic = new DiagonalHeuristic();

        MapFileReader mapFileReader = new MapFileReader(
                "London_2_512.map",
                octileHeader,
                iHeuristic);

        MapView mapView = new MapView(mapFileReader);
        Scene scene = new Scene(mapView.get());
        scene.getStylesheets().add("stylesheet.css");
        stage.setTitle("Tiralab");
        stage.setScene(scene);
        stage.show();
        // Add a general listener to the root view (StackPane), any changes to its children
        // will cause the stage to automagically resize itself to everything.
        mapView.rootChildren().addListener((ListChangeListener.Change<? extends Node> change) -> {
            stage.sizeToScene();
        });
    }

    public static void main(String[] args) {
        launch();
    }

}