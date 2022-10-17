package model.util;

import org.javatuples.*;

import java.util.*;

// Utility class for a general tree: we'll be using these a lot
public abstract class AbstractTree<T> {

    // An AbstractTree holds some kind of data; we'll want this to be generic
    // e.g. a tag, attributes, a tag and attributes, etc
    private T data;
    // Since it's a tree every node also has children.
    private ArrayList<AbstractTree<T>> children;

    // future implementations may want to consider adding an Optional<> parent; or an Optional<> prevSibling

    public T getData() {
        return data;
    }

    public ArrayList<AbstractTree<T>> getChildren() {
        return children;
    }

    // god so much boilerplate
    public AbstractTree(T data, ArrayList<AbstractTree<T>> children) {
        this.data = data;
        this.children = children;
    }

    public void addChild(AbstractTree<T> child) {
        this.children.add(child);
    }
}
