package com.github.alex.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 21/08/2017.
 */
public class Amalgamater {

    private List<ArticleInformation> tree;
    private String query;
    private String wikipediaBaseURL = "https://en.wikipedia.org/wiki";
    private Map<String, ArticleInformation> linksAndCorrespondingNodes;

    public Amalgamater(String query) {
        this.query = query;
    }

    public Amalgamater() {
        this.query = "Dog";
    }

    public void populateNode(ArticleInformation node) {
        try {
            Document doc = Jsoup.connect(node.getArticleLink()).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").get();
            Elements elements = doc.select("p");
            Element firstParagraph = elements.get(1);
            Elements links = firstParagraph.select("a[href]");

            List<ArticleInformation> children = new ArrayList<ArticleInformation>();

            for (Element e : links) {
                if (!e.toString().contains("#cite_note")) { // make sure it is nost redirecting to another point in the article.
                    String articleName = e.attr("title");
                    String articleLink = "https://en.wikipedia.org" + e.attr("href");
                    if (!linksAndCorrespondingNodes.containsKey(articleLink)) { // If the article has not been mentioned before
                        ArticleInformation subArticle = new ArticleInformation(articleName, articleLink);
                        children.add(subArticle);
                        linksAndCorrespondingNodes.put(articleLink, subArticle);
                    } else {
                        children.add(linksAndCorrespondingNodes.get(articleLink));
                    }
                }
            }
            node.setChildren(children);
        } catch (IOException e) {
            ErrorMessage em = new ErrorMessage();
            em.createErrorPopup("Invalid Wikipedia article [non-existent]");
        }
    }

    public List<ArticleInformation> createTree() {
        linksAndCorrespondingNodes = new HashMap<>();
        String baseArticle = wikipediaBaseURL + "/" + query;
        ArticleInformation baseNode = new ArticleInformation(query, baseArticle);
        populateNode(baseNode);
        return baseNode.getChildren();
    }



}
