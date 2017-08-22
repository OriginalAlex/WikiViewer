package com.github.alex.helper;

import java.util.List;

/**
 * Created by Alex on 21/08/2017.
 */
public class ArticleInformation {

    private String article;
    private String articleLink;
    private List<ArticleInformation> childNodes;

    public ArticleInformation(String article, String articleLink, List<ArticleInformation> children) {
        this.article = article;
        this.articleLink = articleLink;
        this.childNodes = children;
    }

    public ArticleInformation(String article, String articleLink) {
        this.article = article;
        this.articleLink = articleLink;
    }

    public void setChildren(List<ArticleInformation> nodes) {
        this.childNodes = nodes;
    }

    // Getters:

    public String getArticleName() {
        return this.article;
    }

    public String getArticleLink() {
        return this.articleLink;
    }

    public List<ArticleInformation> getChildren() {
        return this.childNodes;
    }

}
