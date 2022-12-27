package model.layout;

import model.html.ElementNode;
import model.html.Node;
import model.html.TextNode;

import java.awt.*;

public class InlineLayout extends Layout {

    private Point cursor;

    public InlineLayout(Node node, Layout parent) {
        super(node, parent);
        cursor = new Point();
    }

    // recursively construct the layout tree
    public void layout() {
        this.setLocation(this.getParent().getLocation());
        this.getPreviousSibling().ifPresent(
                sibling -> this.setY(sibling.getY() + sibling.getHeight()));

        this.setWidth(this.getParent().getWidth());
        this.setCursor(this.getX(), this.getY());

        Node node = this.getAssociatedNode();
        switch (node) {
            case ElementNode e -> {
                if (e.getTag().equals("a")) {
                    this.setX(this.getX() + this.getParent().getWidth());
                }
            }
            default -> {
                if (node.getData().length() > 5) {
                    this.setHeight(20);
//                    this.setWidth(this.getWidth() + node.getData().length());
                }
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
