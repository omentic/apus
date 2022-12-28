package model.layout;

import model.html.Node;

import java.awt.*;

public class BlockLayout extends Layout {

    public BlockLayout(Node node, Layout parent) {
        super(node, parent);
    }

    // recursively construct the layout tree
    public void layout() {
        this.location = (Point) this.parent.location.clone();
        this.previousSibling.ifPresent(
                sibling ->  this.location.y = sibling.location.y + sibling.dimension.height);
//        this.previousSibling.ifPresent(
//                sibling -> System.out.println("bluh" + sibling.associatedNode.data()));

//        this.dimension = (Dimension) this.parent.dimension.clone();

        for (Layout child : this.children) {
            child.layout();
            this.dimension.height += child.dimension.height;
        }
//        System.out.println(this.associatedNode.data() + this.location);
//        System.out.println(System.identityHashCode(this.location));
//        System.out.println(this.associatedNode.data() + this.dimension);
    }
}
