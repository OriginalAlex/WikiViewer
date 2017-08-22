package com.github.alex.graph;

import java.awt.Desktop;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.alex.helper.Amalgamater;
import com.github.alex.helper.ArticleInformation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Cell extends Pane {

    private boolean hasBeenExpandedBefore = false;
    private String articleName;
    private String articleLink;
    private List<Cell> children = new ArrayList<>();
    private List<Cell> parents = new ArrayList<>();
    private Text nameDisplayer;

    private Node view;

    public Cell(String articleName, String articleLink) {
        this.articleName = articleName;
        this.articleLink = articleLink;
        setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    if (hasBeenExpandedBefore) {
                        return;
                    }
                    Amalgamater amalgamater = new Amalgamater(articleName);
                    List<ArticleInformation> children = amalgamater.createTree();
                    Model model = Graph.getInstance().getModel();
                    for (ArticleInformation n : children) {
                        if (!model.cellMap.containsKey(n.getArticleName())) {
                            model.addCell(n.getArticleName(), CellType.CIRCLE, n.getArticleLink());
                            model.addEdge(articleName, n.getArticleName());

                        } else {
                            Cell parent = model.cellMap.get(n.getArticleName());
                            Color twoShadesDarker = ((Color) (((Circle) parent.getView()).getFill())).darker().darker();
                            ((Circle) parent.getView()).setFill(twoShadesDarker); // Set the node color two shades darker every time another node references it (aesthetic reasons)
                            ((Circle) parent.getView()).setRadius(((Circle) parent.getView()).getRadius()+2); // Increase the radius by two in addition to the aforementioned.
                            model.addEdge(articleName, parent.getArticleName());
                        }
                    }
                    Graph.getInstance().endUpdate();
                    hasBeenExpandedBefore = true;
                    break;
                case SECONDARY:
                    try {
                        Desktop.getDesktop().browse(new URL(articleLink).toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                }
            }
        });
        setOnMouseEntered(event -> {
            Cell parent = this;
            while (!parent.getCellParents().isEmpty())  {
                ((Circle) parent.getView()).setFill(Color.GOLD);
                ((Circle) parent.getView()).setStroke(Color.GOLD);
                Cell nextParent = parent.getCellParents().get(0);
                parent.toFront();

                List<Cell> lineFinder = Arrays.asList(nextParent, parent);
                ConnectingLine line = ConnectingLine.getLine(lineFinder);
                line.getLine().setStroke(Color.GOLD);
                line.getLine().toFront();
                parent.getNameDisplayer().setFill(Color.GOLD);
                parent.getNameDisplayer().setFont(Font.font("Verdana", FontWeight.BOLD, 17));
                parent.getNameDisplayer().toFront();

                parent = nextParent;
            }
            ((Circle) parent.getView()).setFill(Color.GOLD); // Repeat for the genesis cell
            ((Circle) parent.getView()).setStroke(Color.GOLD);
            parent.getNameDisplayer().setFill(Color.GOLD);
            parent.getNameDisplayer().setFont(Font.font("Verdana", FontWeight.BOLD, 17));
            parent.getNameDisplayer().toFront();
            parent.toFront();

        });
        setOnMouseExited(event -> {
            Cell parent = this;
            while (!parent.getCellParents().isEmpty()) {
                ((Circle) parent.getView()).setFill(Color.TEAL);
                ((Circle) parent.getView()).setStroke(Color.TEAL);
                Cell nextParent = parent.getCellParents().get(0);

                List<Cell> lineFinder = Arrays.asList(nextParent, parent);
                ConnectingLine line = ConnectingLine.getLine(lineFinder);
                line.getLine().setStroke(Color.GRAY);
                parent.getNameDisplayer().setFill(Color.BLACK);
                parent.getNameDisplayer().setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));

                parent = nextParent;
            }
            ((Circle) parent.getView()).setFill(Color.MAGENTA);
            ((Circle) parent.getView()).setStroke(Color.MAGENTA);
            parent.getNameDisplayer().setFill(Color.BLACK);
            parent.getNameDisplayer().setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
        });
    }

    protected void setText(Text text) {
        this.nameDisplayer = text;
    }

    protected Text getNameDisplayer() {
        return nameDisplayer;
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {
        this.view = view;
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getArticleName() {
        return articleName;
    }
}