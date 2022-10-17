package model.html;

import model.util.Node;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Optional;

public class ElementNode implements Node {
    private String tag;
    private ArrayList<Pair<String,String>> attributes;

    private ArrayList<Node> children;

    /**
     * EFFECTS: Constructs a new ElementNode from the arguments provided.
     * MODIFIES: this
     */
    public ElementNode(String tag, ArrayList<Pair<String, String>> attributes, ArrayList<Node> children) {
        this.tag = tag;
        this.attributes = attributes;
        this.children = children;
    }

    /**
     * Overloads the constructor for ease of use. We often don't provide children, at first.
     * EFFECTS: Constructs a new ElementNode from the arguments provided.
     * MODIFIES: this
     */
    public ElementNode(String tag, ArrayList<Pair<String, String>> attributes) {
        this(tag, attributes, new ArrayList<>());
    }

    /**
     * Overloads the constructor for ease of use. Should probably only be used for tests.
     * EFFECTS: Constructs a new ElementNode from the arguments provided.
     * MODIFIES: this
     */
    public ElementNode(String tag) {
        this(tag, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * EFFECTS: Adds a child to the children ArrayList.
     * MODIFIES: this
     */
    public void addChild(Node child) {
        this.children.add(child);
    }

    public String getTag() {
        return this.tag;
    }

    public ArrayList<Pair<String, String>> getAttributes() {
        return this.attributes;
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    // We implement this method for easy debugging.
    public String getData() {
        return getTag() + " " + getAttributes().toString();
    }
}
