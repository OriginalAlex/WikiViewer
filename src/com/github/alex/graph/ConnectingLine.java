package com.github.alex.graph;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 21/08/2017.
 */
public class ConnectingLine extends Group {

    private static Map<List<Cell>, ConnectingLine> lines = new HashMap<>();

    private Cell source;
    private Cell target;

    private Line line;

    public ConnectingLine(Cell source, Cell target) {
        this.source = source;
        this.target = target;
        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();

        line.startXProperty().bind( source.layoutXProperty().add(((Circle) source.getView()).getCenterX()));
        line.startYProperty().bind( source.layoutYProperty().add(((Circle) source.getView()).getCenterY()));

        line.endXProperty().bind( target.layoutXProperty().add(((Circle) target.getView()).getCenterX()));
        line.endYProperty().bind( target.layoutYProperty().add(((Circle) target.getView()).getCenterY()));

        List<Cell> parentAndTarget = Arrays.asList(source, target);
        lines.put(parentAndTarget, this);
        getChildren().add(line);

    }

    public Line getLine() { return line; }

    public Cell getSource() {
        return source;
    }

    public Cell getTarget() {
        return target;
    }

    public static ConnectingLine getLine(List<Cell> cells) {
        return lines.get(cells);
    }

}
