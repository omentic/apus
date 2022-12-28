package model.layout;

import model.html.ElementNode;
import model.html.Node;

import java.awt.*;

public class InlineLayout extends Layout {

    private Point cursor;

    public InlineLayout(Node node, Layout parent) {
        super(node, parent);
        cursor = new Point();
    }

    // recursively construct the layout tree
    public void layout() {
        this.location = (Point) this.parent.location.clone(); // java moment
        this.previousSibling.ifPresent(
                sibling -> this.location.y = sibling.location.y + sibling.dimension.height);

        this.dimension.width = this.parent.dimension.width;
        this.setCursor(this.location.x, this.location.y);

        Node node = this.associatedNode;
        switch (node) {
            case ElementNode e -> {
                if (e.tag.equals("a")) {
                    this.location.x += this.parent.dimension.width;
                }
            }
            default -> {
                if (node.data().length() > 5) {
                    this.dimension.height = 20;
//                    this.dimension.width = this.dimension.width + node.data().length();
                }
            }
        }

        for (Layout child : this.children) {
            child.layout();
            this.dimension.height += child.dimension.height; // fixme
        }

        // todo: recurse to calculate cursor
//        this.height = cursor.location.y - this.location.y;
//        System.out.println(this.associatedNode.data() + this.location);
    }

    public void setCursor(Point cursor) {
        this.cursor = cursor;
    }

    public void setCursor(double x, double y) {
        this.cursor.setLocation(x, y);
    }
}
