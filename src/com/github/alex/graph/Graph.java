package com.github.alex.graph;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;

public class Graph {

    private static Graph INSTANCE = new Graph();
    private Model model;

    private Group canvas;

    private ZoomableScrollPane scrollPane;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */

    public static void createNewInstance() {
        INSTANCE = new Graph();
    }

    public static Graph getInstance() {
        return INSTANCE;
    }

    private CellLayer cellLayer;

    private Graph() {

        this.model = new Model();

        canvas = new Group();
        cellLayer = new CellLayer();

        canvas.getChildren().add(cellLayer);

        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public Pane getCellLayer() {
        return this.cellLayer;
    }

    public Model getModel() {
        return model;
    }

    public void endUpdate() {

        // add components to graph pane
        getCellLayer().getChildren().addAll(model.getAddedEdges());
        model.getAddedEdges().stream().forEach(n -> {
            n.toBack();
            n.getLine().setStroke(Color.GRAY);});
        getCellLayer().getChildren().addAll(model.getAddedCells());

        getModel().attachOrphansToGraphParent(model.getAddedCells());
        getModel().merge();
        List<Cell> cells = getModel().getAllCells();

    }

    public double getScale() {
        return this.scrollPane.getScaleValue();
    }
}