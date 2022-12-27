package ui;

import model.html.*;
import model.layout.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BrowserCanvas extends JPanel {
    private final DocumentLayout currentLayout;

    public BrowserCanvas(ArrayList<Node> html) {
        super();
        this.currentLayout = Layout.constructTree(html);
        printTree(this.currentLayout.getChildren());
    }

    private void printTree(ArrayList<Layout> tree) {
        for (Layout node : tree) {
            System.out.println(System.identityHashCode(node.getLocation()));
            printTree((node).getChildren());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Point location = new Point(10, 20); // we need a mutable reference
        renderHtml(this.currentLayout.getChildren(), g, location);
    }

    private void renderHtml(ArrayList<Layout> tree, Graphics g, Point location) {
        for (Layout layout : tree) {
//            System.out.println(layout.getLocation());
            g.drawRect(layout.getLocation().x, layout.getLocation().y, layout.getDimension().width, layout.getDimension().height);
            if (layout.getAssociatedNode() instanceof TextNode) {
                if (layout.getAssociatedNode().getData().length() > 5) {
//                    System.out.println(location);
                    g.drawString(layout.getAssociatedNode().getData(), layout.getLocation().x, layout.getLocation().y);
                    g.drawString("X", 10, 20);
                }
            } else {
                renderHtml(layout.getChildren(), g, location);
            }
        }
    }
}
