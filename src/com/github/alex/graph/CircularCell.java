package com.github.alex.graph;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by Alex on 21/08/2017.
 */
public class CircularCell extends Cell {

    public CircularCell(String id, String link) {
        super(id, link);

        Circle view = new Circle(7);

        view.setStroke(Color.TEAL);
        view.setFill(Color.TEAL);

        setView(view);
    }

    public void setText(Text txt) {
        txt.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
        super.setText(txt);
    }

}
