package model.layout;

import model.html.ElementNode;
import model.html.Node;

import java.awt.*;
import java.util.*;

public abstract class Layout {
    private Point location;
    private Dimension dimension;

    private Node associatedNode;
    private Layout parent;
    private Optional<Layout> previousSibling;
    private Optional<Layout> nextSibling;
    private ArrayList<Layout> children;

    public static final int DEFAULT_WIDTH = 1920;
    public static final int DEFAULT_HEIGHT = 1080;

    // https://developer.mozilla.org/en-US/docs/Web/HTML/Block-level_elements
    public static final Set<String> BLOCK_ELEMENTS = new HashSet<>(
            Arrays.asList("address", "article", "aside", "blockquote",
                    "details", "dialog", "dd", "div", "dl", "dt",
                    "fieldset", "figcaption", "figure", "footer", "form",
                    "h1", "h2", "h3", "h4", "h5", "h6", "header", "hgroup", "hr",
                    "li", "main", "nav", "ol", "p", "pre", "section", "table", "ul"));

    // the big function
    public abstract void layout();

    public Layout(Node node, Layout parent) {
        this.associatedNode = node;

        this.location = new Point();
        this.dimension = new Dimension();

        this.parent = parent;
        this.previousSibling = Optional.empty();
        this.nextSibling = Optional.empty();
        this.children = new ArrayList<>();
    }

    // eh, probably the best place to put this
    // parent MAY BE nil: a handy call to Optional.ofNullable allows this
    public static ArrayList<Layout> constructTree(ArrayList<Node> html, Layout parent) {
        ArrayDeque<Layout> result = new ArrayDeque<>();
        for (Node node : html) {
            Layout layout;
            if (node instanceof ElementNode) {
                if (BLOCK_ELEMENTS.contains(((ElementNode) node).getTag())) {
                    layout = new BlockLayout(node, parent);
                } else {
                    layout = new InlineLayout(node, parent);
                }
                layout.setChildren(constructTree(((ElementNode) node).getChildren(), layout));
            } else {
                layout = new InlineLayout(node, parent);
            }

            if (result.size() > 0) {
                layout.setPreviousSibling(result.getLast());
                result.getLast().setNextSibling(layout);
            }

            result.add(layout);
        }
        return new ArrayList<>(result); // haha
    }

    public static DocumentLayout constructTree(ArrayList<Node> html) {
        DocumentLayout result = new DocumentLayout();
        result.setChildren(constructTree(html, result));
        return result;
    }

    // man, fuck design patterns, this is so much goddamn code

    public void setLocation(Point point) {
        this.location = point;
    }

    public void setX(double x) {
        this.location.setLocation(x, this.getLocation().getY());
    }

    public void setY(double y) {
        this.location.setLocation(this.getLocation().getX(), y);
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setDimension(double x, double y) {
        this.dimension.setSize(x, y);
    }

    public void setWidth(double width) {
        this.dimension.setSize(width, this.getDimension().getWidth());
    }

    public void setHeight(double height) {
        this.dimension.setSize(this.getDimension().getHeight(), height);
    }

    public void setPreviousSibling(Layout sibling) {
        this.previousSibling = Optional.ofNullable(sibling);
    }

    public void setNextSibling(Layout parent) {
        this.nextSibling = Optional.ofNullable(parent);
    }

    public void setChildren(ArrayList<Layout> children) {
        this.children = children;
    }

    public void addChild(Layout child) {
        this.children.add(child);
    }

    public Node getAssociatedNode() {
        return this.associatedNode;
    }

    public Point getLocation() {
        return this.location;
    }

    public double getX() {
        return this.location.getX();
    }

    public double getY() {
        return this.location.getY();
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public double getWidth() {
        return this.dimension.getWidth();
    }

    public double getHeight() {
        return this.dimension.getHeight();
    }

    public Layout getParent() {
        return this.parent;
    }

    public Optional<Layout> getPreviousSibling() {
        return this.previousSibling;
    }

    public Optional<Layout> getNextSibling() {
        return this.nextSibling;
    }

    public ArrayList<Layout> getChildren() {
        return this.children;
    }
}
