package com.github.alex.wikipedia;

import com.github.alex.graph.*;
import com.github.alex.helper.ErrorMessage;
import com.github.alex.helper.HelperClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Controller {

    private Graph graph;
    private Model model;
    private ErrorMessage em;

    @FXML
    private BorderPane root;

    @FXML
    private Button searchButton;

    @FXML
    private TextArea searchTerm;

    @FXML
    public void initialize() {
        this.em = new ErrorMessage();
        searchTerm.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchRequested();
                event.consume();
            }
        });

        graph = Graph.getInstance();
        root.setCenter(graph.getScrollPane());
        model = graph.getModel();

        createGenesisCell("Dog");
    }

    @FXML
    public void searchRequested() {
        String query = searchTerm.getText();
        if (HelperClass.isValidURL("https://en.wikipedia.org/wiki/" + query.replaceAll(" ", "_"))) {
            Graph.createNewInstance();
            graph = Graph.getInstance();
            root.setCenter(graph.getScrollPane());
            model = graph.getModel();
            searchTerm.clear();

            createGenesisCell(query);
        } else {
            em.createErrorPopup("No such article exists.");
        }
    }

    @FXML
    public void generateRandomArticle() {
        em.createErrorPopup("Work in progress.");
//        try {
//            Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Special:Random").userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                    .referrer("http://www.google.com").get();
//            String randomLoc = doc.location();
//            String name = randomLoc.split("/wiki/")[1];
//            createGenesisCell(name.replaceAll("_", " "));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void createGenesisCell(String name) {
        graph.getCellLayer().getChildren().clear();
        model.addCell(name, CellType.CIRCLE, "https://en.wikipedia.org/wiki/" + name.replaceAll(" ", "_"));
        Cell genesis = model.getAddedCells().get(0);
        graph.endUpdate();
        genesis.setLayoutX(490);
        genesis.setLayoutY(350); // center the genesis cell!!!!
        Graph.getInstance().getCellLayer().getChildren().remove(0);
        Text text = new Text(genesis.getArticleName());
        text.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
        Graph.getInstance().getCellLayer().getChildren().add(text);

        Circle visualRepresentation = (Circle) genesis.getView();

        visualRepresentation.setFill(Color.MAGENTA); // Give the genesis cell a different color for visibility purposes (it will of course be centered regardless)
        visualRepresentation.setStroke(Color.MAGENTA);

        text.setX(490 - text.getLayoutBounds().getWidth() / 2.0); // Center it.
        text.setY(336);
        ((CircularCell) genesis).setText(text);
    }

    private boolean isValidWikiArticle(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
