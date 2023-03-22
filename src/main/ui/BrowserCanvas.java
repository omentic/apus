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
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        g.setColor(Color.WHITE);
        renderHtml(this.currentLayout.children, g);
    }

    private void renderHtml(ArrayList<Layout> tree, Graphics g) {
        for (Layout layout : tree) {
            g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.drawRect(layout.location.x, layout.location.y, layout.dimension.width, layout.dimension.height);
            g.setColor(Color.WHITE);

            if (layout.associatedNode instanceof TextNode t) {
                g.drawString(t.text(), layout.location.x, layout.location.y + layout.dimension.height - 5);
            } else {
                renderHtml(layout.children, g);
            }
        }
    }
}
