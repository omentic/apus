package model.layout;

import model.html.*;

import java.awt.*;
import java.util.*;

// Generic Layout class
public abstract class Layout {
    // fuck encapsulation all my homies hate encapsulation
    // but seriously, what a garbage idea: get a better language
    // (please read the above comment in the voice of https://www.youtube.com/watch?v=EdWSg6YwUeo)
    public final Point location;
    public final Dimension dimension;

    public final Node associatedNode;
    public final Layout parent;
    public Optional<Layout> previousSibling;
    public Optional<Layout> nextSibling;
    public ArrayList<Layout> children;

    public static final int DEFAULT_X = 10;
    public static final int DEFAULT_Y = 20;
    public static final int DEFAULT_WIDTH = 1000;
    public static final int DEFAULT_HEIGHT = 800;
    public static final int TEXT_WIDTH_CONSTANT = 7;
    public static final int TEXT_HEIGHT_CONSTANT = 20;

    // https://developer.mozilla.org/en-US/docs/Web/HTML/Block-level_elements
    public static final Set<String> BLOCK_ELEMENTS = new HashSet<>(
            Arrays.asList("address", "article", "aside", "blockquote", "body",
                    "details", "dialog", "dd", "div", "dl", "dt",
                    "fieldset", "figcaption", "figure", "footer", "form",
                    "h1", "h2", "h3", "h4", "h5", "h6", "header", "hgroup", "hr",
                    "li", "main", "nav", "ol", "p", "pre", "section", "table", "ul"));

    public static final Set<String> HIDDEN_ELEMENTS = new HashSet<>(
        Arrays.asList("head", "meta", "link", "title"));

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
        var result = new ArrayDeque<Layout>();
        for (Node node : html) {
            Layout layout;
            switch (node) {
                case ElementNode e -> {
                    if (BLOCK_ELEMENTS.contains(e.tag)) {
                        layout = new BlockLayout(e, parent);
                    } else {
                        layout = new InlineLayout(e, parent);
                    }
                    layout.children = constructTree(e.children, layout);
                }
                case TextNode t -> {
                    layout = new TextLayout(t, parent);
                }
                default -> throw new IllegalStateException("Unexpected value: " + node);
            }

            if (result.size() > 0) {
                layout.previousSibling = Optional.of(result.getLast());
                result.getLast().nextSibling = Optional.of(layout);
            }
            result.add(layout);
        }
        return new ArrayList<>(result); // haha
    }

    public static DocumentLayout constructTree(ArrayList<Node> html) {
        var result = new DocumentLayout();
        result.children = constructTree(html, result);
        result.layout();
        return result;
    }
}
