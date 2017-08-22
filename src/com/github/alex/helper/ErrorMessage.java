package com.github.alex.helper;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Alex on 21/08/2017.
 */
public class ErrorMessage {

    public void createErrorPopup(String reason) {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        Text text = new Text("ERROR:");
        text.setFill(Color.CRIMSON);
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        Text msg = new Text(reason);
        msg.setFill(Color.CRIMSON);
        msg.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));

        container.getChildren().addAll(text, msg);

        Scene scene = new Scene(container);
        Stage popup = new Stage();
        popup.setResizable(false);
        popup.setTitle("ERROR");
        popup.setScene(scene);

        popup.show();

    }

}
