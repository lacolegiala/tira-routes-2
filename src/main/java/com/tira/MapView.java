package com.tira.tiramap;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MapView {
    private VBox vbox;
    private StackPane stackPane;

    private WritableImage writableImage;

    private ImageView imageView;

    private MapFileReader mapFileReader;

    public MapView(MapFileReader mapFileReader) {
        this.mapFileReader = mapFileReader;
        writableImage = new WritableImage(mapFileReader.getMapGrid().getSizeX(),
                mapFileReader.getMapGrid().getSizeY()
                );

        this.setImage(mapFileReader.getMapGrid());
        imageView = new ImageView(writableImage);
        this.vbox = new VBox(imageView);
        this.stackPane = new StackPane(this.vbox);
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
     * @return
     */
    public ObservableList<Node> rootChildren() {
        return this.stackPane.getChildren();
    }

    /**
     * Returns the underlying StackPane on which the rest of the elements are added.
     * @return
     */
    public StackPane get() {
        return this.stackPane;
    }

}
