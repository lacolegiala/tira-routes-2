package com.tira;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MapView {
    private VBox vbox;

    private VBox vboxInputFields;
    private GridPane gridPane;

    private WritableImage writableImage;

    private ImageView imageView;

    private MapFileReader mapFileReader;

    private int startX = 0 ;
    private int startY = 0 ;

    private int targetX = 0 ;
    private int targetY = 0 ;


    public MapView(MapFileReader mapFileReader) {
        this.mapFileReader = mapFileReader;
        writableImage = new WritableImage(mapFileReader.getMapGrid().getSizeX(),
                mapFileReader.getMapGrid().getSizeY()
        );

        this.setImage(mapFileReader.getMapGrid());
        imageView = new ImageView(writableImage);
        this.vbox = new VBox(imageView);

        this.gridPane = new GridPane();
        this.gridPane.setPadding(new Insets(10, 10, 10, 10));
        this.gridPane.setVgap(5);
        this.gridPane.setHgap(5);

        this.vboxInputFields = addInputFields();

        GridPane.setRowIndex(this.vbox, 0);
        GridPane.setRowIndex(this.vboxInputFields, 1);

        this.gridPane.getChildren().addAll(this.vbox, this.vboxInputFields);
        ;
    }

    private VBox addInputFields() {
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        GridPane gridCoordinates = new GridPane();
        gridCoordinates.setPadding(new Insets(10, 10, 10, 10));
        Label lStart = new Label("Start");
        Label lsX = new Label("X:");
        Label lsY = new Label("Y:");
        Label ltX = new Label("X:");
        Label ltY = new Label("Y:");
        Label lTarget = new Label("Target");
        TextField startX = new TextField();
        TextField startY = new TextField();
        TextField targetX = new TextField();
        TextField targetY = new TextField();

        startX.textProperty().addListener((observable, oldValue, newValue) ->
        {
            // TODO: add checking that the new start coord is reasonable: within limits (and not inside a blocked area?)
            this.startX = Integer.valueOf(newValue);
            coordinatesChangedLogging();
            showEndpoints();
        });

        startY.textProperty().addListener((observable, oldValue, newValue) ->
        {
            // TODO: add checking that the new start coord is reasonable
            this.startY = Integer.valueOf(newValue);
            coordinatesChangedLogging();
            showEndpoints();
        });

        targetX.textProperty().addListener((observable, oldValue, newValue) ->
        {
            // TODO: add checking that the new start coord is reasonable
            this.targetX = Integer.valueOf(newValue);
            coordinatesChangedLogging();
            showEndpoints();
        });

        targetY.textProperty().addListener((observable, oldValue, newValue) ->
        {
            // TODO: add checking that the new start coord is reasonable
            this.targetY = Integer.valueOf(newValue);
            coordinatesChangedLogging();
            showEndpoints();
        });

        GridPane.setConstraints(lStart, 0, 0);  // column 0, row 0: Start
        GridPane.setConstraints(lsX, 1, 0);  // column 1, row 0: X
        GridPane.setConstraints(startX, 2, 0);  // column 2, row 0: input text for X
        GridPane.setConstraints(lsY, 3, 0);  // column 3, row 0: Y
        GridPane.setConstraints(startY, 4, 0);  // column 4, row 0: input text for Y

        GridPane.setConstraints(lTarget, 0, 1);  // column 0, row 1: Target
        GridPane.setConstraints(ltX, 1, 1);  // column 1, row 1: X
        GridPane.setConstraints(targetX, 2, 1);  // column 2, row 1: input text for X
        GridPane.setConstraints(ltY, 3, 1);  // column 3, row 1: Y
        GridPane.setConstraints(targetY, 4, 1);  // column 4, row 1: input text for Y

        gridCoordinates.getChildren().addAll(lStart, lsX, startX, lsY, startY,
                lTarget, ltX, targetX, ltY, targetY);


        GridPane.setConstraints(gridCoordinates, 0, 0);

        grid.getChildren().add(gridCoordinates);

        //Defining the Submit button
        Button submit = new Button("Start");
        GridPane.setConstraints(submit, 0, 2);
        grid.getChildren().add(submit);

        submit.setOnAction(event -> showEndpoints());
        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 2);
        grid.getChildren().add(clear);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(grid);
        return vbox;
    }

    void coordinatesChangedLogging() {
        System.out.println("start X now " + this.startX);
        System.out.println("start Y now " + this.startY);
        System.out.println("target X now " + this.targetX);
        System.out.println("target Y now " + this.targetY);

    }

    private void showEndpoints() {
        // update the image with the pixels
        for (int y = 0; y < this.mapFileReader.getMapGrid().getSizeX(); y++) {
            for (int x = 0; x < this.mapFileReader.getMapGrid().getSizeX(); x++) {
                // use color near the start and target points to visualize them
                if (Math.abs(x - this.startX) < 4 && Math.abs(y - startY) < 4) {
                    writableImage.getPixelWriter().setColor(x, y, Color.RED);
                } else
                if (Math.abs(x - this.targetX) < 4 && Math.abs(y - targetY) < 4) {
                    writableImage.getPixelWriter().setColor(x, y, Color.YELLOW);
                } else
                if (this.mapFileReader.getMapGrid().getGrid()[x][y].getNodeType() == NodeType.FREE) {
                    writableImage.getPixelWriter().setColor(x, y, Color.GREY);
                } else {
                    writableImage.getPixelWriter().setColor(x, y, Color.BLACK);
                }
            }
        }

    }

    public void setImage(MapGrid mapGrid) {
        // update the image with the pixels
        for (int y = 0; y < mapGrid.getSizeY(); y++) {
            for (int x = 0; x < mapGrid.getSizeX(); x++) {
                if (mapGrid.getGrid()[x][y].getNodeType() == NodeType.FREE) {
                    writableImage.getPixelWriter().setColor(x, y, Color.GREY);
                } else {
                    writableImage.getPixelWriter().setColor(x, y, Color.BLACK);
                }
            }
        }
    }

    /**
     * Return the ObservableList of the root node of this class, used for resizing
     * purposes in the App class
     *
     * @return
     */
    public ObservableList<Node> rootChildren() {
        return this.gridPane.getChildren();
    }

    /**
     * Returns the underlying StackPane on which the rest of the elements are added.
     *
     * @return
     */
    public GridPane get() {
        return this.gridPane;
    }

}
