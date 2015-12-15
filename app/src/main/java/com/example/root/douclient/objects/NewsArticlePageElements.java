package com.example.root.douclient.objects;

/**
 * Created by root on 15.12.15.
 */
public class NewsArticlePageElements {

    private String elementType;
    private String elementContent;

    public NewsArticlePageElements(String elementType, String elementContent) {
        this.elementType = elementType;
        this.elementContent = elementContent;
    }

    public String getElementType() {
        return elementType;
    }

    public String getElementContent() {
        return elementContent;
    }
}
