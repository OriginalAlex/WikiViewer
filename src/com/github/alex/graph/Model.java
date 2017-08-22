package com.github.alex.graph;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.*;

public class Model {

    private Cell graphParent;

    private List<Cell> allCells;
    private List<Cell> addedCells;
    private List<Cell> removedCells;

    private List<ConnectingLine> allEdges;
    private List<ConnectingLine> addedEdges;
    private List<ConnectingLine> removedEdges;

    Map<List<Cell>, ConnectingLine> lines;

    Map<String,Cell> cellMap; // <id,cell>

    public Model() {

        graphParent = new Cell("_ROOT_", "");

        // clear model, create lists
        clear();
    }

    public void clear() {

        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<Cell> getAddedCells() {
        return addedCells;
    }

    public List<Cell> getRemovedCells() {
        return removedCells;
    }

    public List<Cell> getAllCells() {
        return allCells;
    }

    public List<ConnectingLine> getAddedEdges() {
        return addedEdges;
    }

    public List<ConnectingLine> getRemovedEdges() {
        return removedEdges;
    }

    public List<ConnectingLine> getAllEdges() {
        return allEdges;
    }

    public void addCell(String id, CellType type, String articleLink) {

        switch (type) {

            case CIRCLE:
                CircularCell rectangleCell = new CircularCell(id, articleLink);
                addCell(rectangleCell);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    private void addCell(Cell cell) {
        addedCells.add(cell);
        cellMap.put( cell.getArticleName(), cell);
        Random rnd = new Random();
        List<Cell> parents = cell.getCellParents();
        if (!parents.isEmpty()) {
            Cell parent = parents.get(parents.size()-1);
            System.out.println(parent.getScaleX());
        }
        Scene sc =  Graph.getInstance().getCellLayer().getScene();
        double x = 0;
        double y = 0;
        if (sc == null) {
            x = rnd.nextDouble() * 1000;
            y = rnd.nextDouble() * 700; // The height of the top bar
        } else {
            x = rnd.nextDouble() * sc.getWidth(); // Make it conform to the size of the scene.
            y = rnd.nextDouble() * (sc.getHeight() - 70);
        }
        cell.relocate(x, y);

        Text text = new Text(cell.getArticleName());
        Graph.getInstance().getCellLayer().getChildren().add(text);
        text.setX(x - text.getLayoutBounds().getWidth() / 2.0);
        text.setY(y - 14);
        cell.setText(text);
    }

    public void addEdge( String sourceId, String targetId) {
        Cell sourceCell = cellMap.get(sourceId);
        Cell targetCell = cellMap.get(targetId);

        ConnectingLine edge = new ConnectingLine(sourceCell, targetCell);
        addedEdges.add(edge);

    }

    public void attachOrphansToGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            if( cell.getCellParents().size() == 0) {
                graphParent.addCellChild( cell);
            }
        }

    }

    public void disconnectFromGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            graphParent.removeCellChild( cell);
        }
    }

    public void merge() {

        // cells
        allCells.addAll( addedCells);
        allCells.removeAll(removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll( addedEdges);
        allEdges.removeAll( removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}