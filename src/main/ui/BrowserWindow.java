package ui;

import model.BrowserState;
import model.html.HtmlParser;

import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.util.*;

// Broad JFrame usage taken from here: https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
public class BrowserWindow extends JFrame {
    public static final int WIDTH = 1500;
    public static final int HEIGHT = 500;

    private BrowserCanvas canvas;
    private final BrowserBar browserBar;
    private final BrowserState state;

    public BrowserWindow() {
        super("apus");
        state = new BrowserState(new ArrayDeque<>(), "");

        canvas = new BrowserCanvas(new ArrayList<>());
        browserBar = new BrowserBar(this);
        getContentPane().add(browserBar, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        render("/home/apropos/Projects/website/j-james/index.html");
//        browserBar.addTab("/home/apropos/Projects/website/j-james/index.html");
        setVisible(true);
    }

    public void render(String uri) {
        state.currentTab = uri;
        remove(canvas);
        try {
            String file = Files.readString(Path.of(state.currentTab));
            HtmlParser parser = new HtmlParser();
            canvas = new BrowserCanvas(parser.parseHtml(file));
        } catch (Exception e) {
            System.out.println("Could not read file, rendering empty page: " + e.getMessage());
            canvas = new BrowserCanvas(new ArrayList<>());
        }
        add(canvas);
        repaint();
        setVisible(true);
    }

    public BrowserState getBrowserState() {
        return this.state;
    }
}
