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
        printTree(this.currentLayout.children);
    }

    private void printTree(ArrayList<Layout> tree) {
        for (Layout node : tree) {
//            System.out.println(System.identityHashCode(node.location));
            printTree((node).children);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Point location = new Point(10, 20); // we need a mutable reference
        renderHtml(this.currentLayout.children, g, location);
    }

    private void renderHtml(ArrayList<Layout> tree, Graphics g, Point location) {
        for (Layout layout : tree) {
//            System.out.println(layout.location);
            g.drawRect(layout.location.x, layout.location.y, layout.dimension.width, layout.dimension.height);
            if (layout.associatedNode instanceof TextNode) {
                if (layout.associatedNode.data().length() > 5) {
//                    System.out.println(location);
                    g.drawString(layout.associatedNode.data(), layout.location.x, layout.location.y);
                    g.drawString("X", 10, 20);
                }
            } else {
                renderHtml(layout.children, g, location);
            }
        }
    }
}
