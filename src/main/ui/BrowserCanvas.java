package ui;

import model.html.ElementNode;
import model.html.TextNode;
import model.util.Node;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BrowserCanvas extends JPanel {
    private ArrayList<Node> html;

    // MODIFIES: this
    // EFFECTS: constructs a BrowserCanvas object
    public BrowserCanvas(ArrayList<Node> html) {
        super();
        this.html = html;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Point location = new Point(10, 20); // we need a mutable reference
        renderHtml(html, g, location);
    }

    // EFFECTS: naively renders our html file by printing text nodes
    private void renderHtml(ArrayList<Node> html, Graphics g, Point location) {
        for (Node node : html) {
            if (node instanceof TextNode) {
                if (!node.getData().isBlank()) {
                    g.drawString(node.getData(), location.x, location.y);
                    location.translate(0, 20);
                }
            } else {
                renderHtml(((ElementNode) node).getChildren(), g, location);
            }
        }
    }

}
