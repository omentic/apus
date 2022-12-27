package model.layout;

import model.html.Node;

import java.awt.*;

public class BlockLayout extends Layout {

    public BlockLayout(Node node, Layout parent) {
        super(node, parent);
    }

    // recursively construct the layout tree
    public void layout() {
        this.setLocation(this.getParent().getLocation());
        this.getPreviousSibling().ifPresent(
                sibling -> this.setY(sibling.getY() + sibling.getHeight()));
        this.getPreviousSibling().ifPresent(
                sibling -> System.out.println("bluh" + sibling.getAssociatedNode().getData()));

//        this.setDimension(this.getParent().getDimension());

        for (Layout child : this.getChildren()) {
            child.layout();
            this.setHeight(this.getHeight() + child.getHeight());
        }
//        System.out.println(this.getAssociatedNode().getData() + this.getLocation());
//        System.out.println(System.identityHashCode(this.getLocation()));
//        System.out.println(this.getAssociatedNode().getData() + this.getDimension());
    }
}
