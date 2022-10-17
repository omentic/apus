package model.html;

import model.util.Node;

public class TextNode implements Node {
    private String text = "";

    public String getText() {
        return this.text;
    }

    public TextNode(String text) {
        this.text = text;
    }

    public String getData() {
        return getText();
    }
}
