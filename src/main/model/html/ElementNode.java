package model.html;

import model.util.Node;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Optional;

public class ElementNode implements Node {
    private String tag;
    private ArrayList<Pair<String,String>> attributes;

    private ArrayList<Node> children;

    public String getTag() {
        return this.tag;
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    public ElementNode(String tag, ArrayList<Pair<String, String>> attributes, ArrayList<Node> children) {
        this.tag = tag;
        this.attributes = attributes;
        this.children = children;
    }

    public ElementNode(String tag, ArrayList<Pair<String, String>> attributes) {
        this(tag, attributes, new ArrayList<Node>());
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public String getData() {
        return getTag();
    }
}
