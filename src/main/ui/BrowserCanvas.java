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
        this.setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Point location = new Point(10, 20); // we need a mutable reference
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        g.setColor(Color.WHITE);
        g.drawString("X", location.x, location.y);
        renderHtml(this.currentLayout.children, g, location);
    }

    private void renderHtml(ArrayList<Layout> tree, Graphics g, Point location) {
        for (Layout layout : tree) {
            g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.drawRect(layout.location.x, layout.location.y, layout.dimension.width, layout.dimension.height);
            g.setColor(Color.WHITE);
            if (layout.associatedNode instanceof TextNode) {
                g.drawString(layout.associatedNode.data(), layout.location.x, layout.location.y + layout.dimension.height - 5);
            } else {
                renderHtml(layout.children, g, location);
            }
        }
    }
}
