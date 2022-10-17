package model.html;

import model.util.Node;

/**
 * This TextNode class represents raw text, with no nested tags.
 */
public class TextNode implements Node {
    private String text = "";

    /**
     * EFFECTS: Creates a new TextNode from the provided String value.
     * MODIFIES: this
     */
    public TextNode(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    // We implement this method for easy debugging.
    public String getData() {
        return getText();
    }
}
