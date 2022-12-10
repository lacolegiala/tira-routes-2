package com.tira;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Stack;

@Log4j2
public class MapView implements IUpdateView {
    private VBox vboxImage;
    private VBox vboxInputFields;
    private GridPane gridPane;

    private WritableImage writableImage;

    private ImageView imageView;

    private ScrollPane zoomPane;

    private MapFileReader mapFileReader;

    private int startX = 0;
    private int startY = 0;

    private int targetX = 0;
    private int targetY = 0;

    private boolean isFinding = false;

    private boolean found = false;

    private Stack<GridNode> route = null;

    public MapView(MapFileReader mapFileReader) {
        this.mapFileReader = mapFileReader;
        writableImage = new WritableImage(mapFileReader.getMapGrid().getSizeX(),
                mapFileReader.getMapGrid().getSizeY()
        );

        setImage(mapFileReader.getMapGrid());
        imageView = new ImageView(writableImage);
        vboxImage = new VBox(imageView);
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        vboxInputFields = addInputFields();
        // zoomPane = createZoomPane(vboxImage);  skip zooming for now
        // GridPane.setRowIndex(zoomPane, 0);
        GridPane.setRowIndex(vboxImage, 0);
        GridPane.setRowIndex(vboxInputFields, 1);

        // gridPane.getChildren().addAll(zoomPane, vboxInputFields);
        gridPane.getChildren().addAll(vboxImage, vboxInputFields);
    }

    private ScrollPane createZoomPane(final VBox vbox) {
        final double SCALE_DELTA = 1.1;
        final StackPane zoomPane = new StackPane();

        zoomPane.getChildren().add(vbox);

        final ScrollPane scroller = new ScrollPane();
        final Group scrollContent = new Group(zoomPane);
        scroller.setContent(scrollContent);

        scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable,
                                Bounds oldValue, Bounds newValue) {
                zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });

        scroller.setPrefViewportWidth(this.startX);
        scroller.setPrefViewportHeight(this.startY);

        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
                        : 1 / SCALE_DELTA;

                // amount of scrolling in each direction in scrollContent coordinate
                // units
                Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);

                vbox.setScaleX(vbox.getScaleX() * scaleFactor);
                vbox.setScaleY(vbox.getScaleY() * scaleFactor);

                // move viewport so that old center remains in the center after the
                // scaling
                repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);

            }
        });

        // Panning via drag....
        final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
        scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
            }
        });

        scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double deltaX = event.getX() - lastMouseCoordinates.get().getX();
                double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
                double deltaH = deltaX * (scroller.getHmax() - scroller.getHmin()) / extraWidth;
                double desiredH = scroller.getHvalue() - deltaH;
                scroller.setHvalue(Math.max(0, Math.min(scroller.getHmax(), desiredH)));

                double deltaY = event.getY() - lastMouseCoordinates.get().getY();
                double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
                double deltaV = deltaY * (scroller.getHmax() - scroller.getHmin()) / extraHeight;
                double desiredV = scroller.getVvalue() - deltaV;
                scroller.setVvalue(Math.max(0, Math.min(scroller.getVmax(), desiredV)));
            }
        });

        return scroller;
    }

    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2;
            double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
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
            if (NumberUtils.isCreatable(newValue)) {
                this.startX = Integer.valueOf(newValue);
                coordinatesChangedLogging();
                clearSearchData();
                showEndpoints();
            }
        });

        startY.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (NumberUtils.isCreatable(newValue)) {
                this.startY = Integer.valueOf(newValue);
                coordinatesChangedLogging();
                clearSearchData();
                showEndpoints();
            }

        });

        targetX.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (NumberUtils.isCreatable(newValue)) {
                this.targetX = Integer.valueOf(newValue);
                coordinatesChangedLogging();
                clearSearchData();
                showEndpoints();
            }
        });

        targetY.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (NumberUtils.isCreatable(newValue)) {
                this.targetY = Integer.valueOf(newValue);
                coordinatesChangedLogging();
                clearSearchData();
                showEndpoints();
            }
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

        // Add buttons to start the search
        Button submit = new Button("Start");
        GridPane.setConstraints(submit, 0, 2);
        grid.getChildren().add(submit);

        submit.setOnAction(event -> showEndpoints());
        //Defining the Find button
        Button clear = new Button("IDA*");
        GridPane.setConstraints(clear, 1, 2);
        grid.getChildren().add(clear);
        clear.setOnAction(event -> findRoute());
        VBox vbox = new VBox();
        vbox.getChildren().addAll(grid);

        // TODO: add fields to show the timing
        return vbox;
    }

    void coordinatesChangedLogging() {
        log.debug("start: ({},{}) target: ({},{})", startX, startY, targetX, targetY);
    }

    private void clearSearchData() {
        found = false;
        route = null;
        for (int y = 0; y < mapFileReader.getMapGrid().getSizeX(); y++) {
            for (int x = 0; x < mapFileReader.getMapGrid().getSizeX(); x++) {
                mapFileReader.getMapGrid().getGrid()[x][y].setSearching(false);
                mapFileReader.getMapGrid().getGrid()[x][y].setChecked(false);
            }
        }
    }

    private void showEndpoints() {
        // update the image with the pixels
        for (int y = 0; y < mapFileReader.getMapGrid().getSizeX(); y++) {
            for (int x = 0; x < mapFileReader.getMapGrid().getSizeX(); x++) {
                // use color near the start and target points to visualize them
                if (Math.abs(x - startX) < 4 && Math.abs(y - startY) < 4) {
                    writableImage.getPixelWriter().setColor(x, y, Color.RED);
                } else if (Math.abs(x - targetX) < 4 && Math.abs(y - targetY) < 4) {
                    writableImage.getPixelWriter().setColor(x, y, Color.YELLOW);
                } else if (mapFileReader.getMapGrid().getGrid()[x][y].getNodeType() == NodeType.FREE) {
                    writableImage.getPixelWriter().setColor(x, y, Color.GREY);
                } else {
                    writableImage.getPixelWriter().setColor(x, y, Color.BLACK);
                }
                if (mapFileReader.getMapGrid().getGrid()[x][y].isSearching()) {
                    writableImage.getPixelWriter().setColor(x, y, Color.CYAN);
                }
                if (found && route != null) {
                    GridNode testNode = new GridNode(x, y, NodeType.FREE);
                    if (route != null && route.contains(testNode)) {
                        writableImage.getPixelWriter().setColor(x, y, Color.LAWNGREEN);
                    }
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
        return gridPane.getChildren();
    }

    /**
     * Returns the underlying StackPane on which the rest of the elements are added.
     *
     * @return
     */
    public GridPane get() {
        return gridPane;
    }

    private void findRoute() {
        found = false;
        route = null;
        GridNode goal = new GridNode(targetX, targetY, NodeType.FREE);
        log.debug("finding route to {}", goal);

        IHeuristic iHeuristicManhattan = new ManhattanHeuristic();
        IHeuristic iHeuristicDiagonal = new DiagonalHeuristic();
        IterativeDeepeningAStar iterativeDeepeningAStar = new IterativeDeepeningAStar(goal, mapFileReader.getMapGrid(), this);
        GridNode start = new GridNode(startX, startY, NodeType.FREE);

        isFinding = true;
        Task task = new Task<Void>() {

            @Override
            public Void call() {
                Stack<GridNode> path = iterativeDeepeningAStar.idaStar(start);
                if (path != null) {
                    found = true;
                    route = path;
                    log.debug("Found: route {}", route);
                    updateView();
                }
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            isFinding = false;
            if (found) {

            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateView() {
        Platform.runLater(() -> {
            showEndpoints();
        });
    }
}
