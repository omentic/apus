package model.html;

/**
 * This TextNode class represents raw text, with no nested tags.
 */
public record TextNode(String text) implements Node {

    // We implement this method for easy debugging.
    public String data() {
        return text();
    }
}
