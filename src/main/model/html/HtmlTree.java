package model.html;

import model.util.AbstractTree;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Representation of HTML as a tree of nodes. Sorry about the generics.
 */
public class HtmlTree extends AbstractTree<Pair<String, ArrayList<Pair<String, String>>>> {
    private String tag;
    private ArrayList<Pair<String, String>> attributes;
    private Optional<HtmlTree> parent = Optional.empty();
    private Optional<HtmlTree> sibling = Optional.empty();

    // I don't quite know why I can't say ArrayList<HtmlTree> children.
    public HtmlTree(String tag, ArrayList<Pair<String, String>> attributes,
            ArrayList<AbstractTree<Pair<String, ArrayList<Pair<String, String>>>>> children,
            Optional<HtmlTree> parent, Optional<HtmlTree> sibling) {
        super(new Pair<>(tag, attributes), children);
        this.tag = tag;
        this.attributes = attributes;
        this.parent = parent;
        this.sibling = sibling;
    }

    public HtmlTree(String tag, ArrayList<Pair<String, String>> attributes) {
        this(tag, attributes, new ArrayList<AbstractTree<Pair<String, ArrayList<Pair<String, String>>>>>(),
                Optional.empty(), Optional.empty());
    }
}
