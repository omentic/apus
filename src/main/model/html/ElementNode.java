package model.html;

import org.javatuples.Pair;

import java.util.ArrayList;

/**
 * This ElementNode class represents an HTML tag and nested tags.
 */
public class ElementNode implements Node {
    public final String tag;
    public final ArrayList<Pair<String,String>> attributes;

    public final ArrayList<Node> children;

    public ElementNode(String tag, ArrayList<Pair<String, String>> attributes, ArrayList<Node> children) {
        this.tag = tag;
        this.attributes = attributes;
        this.children = children;
    }

    /**
     * Overloads the constructor for ease of use. We often don't provide children, at first.
     */
    public ElementNode(String tag, ArrayList<Pair<String, String>> attributes) {
        this(tag, attributes, new ArrayList<>());
    }

    /**
     * Overloads the constructor for ease of use. Should probably only be used for tests.
     */
    public ElementNode(String tag) {
        this(tag, new ArrayList<>(), new ArrayList<>());
    }

    // We implement this method for easy debugging.
    public String data() {
        return this.tag + " " + this.attributes.toString();
    }
}
