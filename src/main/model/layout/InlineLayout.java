package model.layout;

import model.html.Node;

import java.awt.*;

public class InlineLayout extends Layout {

    private Point cursor;

    public InlineLayout(Node node, Layout parent) {
        super(node, parent);
        cursor = new Point();
    }

    public void layout() {
        this.setLocation(this.getParent().getLocation());
        this.getPreviousSibling().ifPresent(
                sibling -> this.setY(sibling.getY() + sibling.getHeight()));

        this.setWidth(this.getParent().getWidth());
        this.setCursor(this.getX(), this.getY());

        for (Layout child : this.getChildren()) {
            child.layout();
        }

        // todo: recurse to calculate cursor
        this.setHeight(cursor.getY() - this.getY());
    }

    public void setCursor(Point cursor) {
        this.cursor = cursor;
    }

    public void setCursor(double x, double y) {
        this.cursor.setLocation(x, y);
    }
}
