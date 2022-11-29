package com.tira;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MapApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        OctileHeader octileHeader = new OctileHeader();
        MapFileReader mapFileReader = new MapFileReader(
                "NewYork_2_256.map",
                octileHeader);

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