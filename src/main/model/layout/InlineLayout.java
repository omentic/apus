package model.layout;

import model.html.ElementNode;
import model.html.Node;
import model.html.TextNode;

import java.awt.*;

// Inline layout style
public class InlineLayout extends Layout {

    private Point cursor;

    // Constructs a new InlineLayout
    public InlineLayout(Node node, Layout parent) {
        super(node, parent);
        cursor = new Point();
    }

    // MODIFIES: this
    // EFFECTS: recursively constructs the layout tree
    public void layout() {
        this.setLocation(this.getParent().getLocation());
        this.getPreviousSibling().ifPresent(
                sibling -> this.setY(sibling.getY() + sibling.getHeight()));

        this.setWidth(this.getParent().getWidth());
        this.setCursor(this.getX(), this.getY());

        Node node = this.getAssociatedNode();
        if (node instanceof TextNode) {
            if (node.getData().length() > 5) {
                this.setHeight(20);
//                this.setWidth(this.getWidth() + node.getData().length());
            }
        } else if (node instanceof ElementNode) {
            if (((ElementNode) node).getTag().equals("a")) {
                this.setX(this.getX() + this.getParent().getWidth());
            }
        }

        for (Layout child : this.getChildren()) {
            child.layout();
            this.setHeight(this.getHeight() + child.getHeight()); // fixme
        }

        // todo: recurse to calculate cursor
//        this.setHeight(cursor.getY() - this.getY());
//        System.out.println(this.getAssociatedNode().getData() + this.getLocation());
    }

    public void setCursor(Point cursor) {
        this.cursor = cursor;
    }

    public void setCursor(double x, double y) {
        this.cursor.setLocation(x, y);
    }
}
