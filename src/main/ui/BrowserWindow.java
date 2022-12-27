package ui;

import model.BrowserState;
import model.html.HtmlParser;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;

// Broad JFrame usage taken from here: https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
public class BrowserWindow extends JFrame {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;

    private BrowserCanvas canvas;
    private BrowserBar browserBar;

    private BrowserState state;

    // MODIFIES: this
    // EFFECTS: creates a new BrowserWindow program for rendering pages
    public BrowserWindow() {
        super("apus");
        state = new BrowserState(new ArrayDeque<>(), "");

        canvas = new BrowserCanvas(new ArrayList<>());
//        render("data/example.html");
        browserBar = new BrowserBar(this);
        getContentPane().add(browserBar, BorderLayout.SOUTH);
//        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        render("/home/apropos/Projects/website/j-james/index.html");
//        render("data/example.hctml");
//        browserBar.addTab("/home/apropos/Projects/website/j-james/index.html");
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Renders an arbitrary page
    public void render(String uri) {
        state.setCurrentTab(uri);
        remove(canvas);
//        System.out.println(state.getCurrentTab());
        try {
            Path path = Paths.get(state.getCurrentTab());
            String file = new String(Files.readAllBytes(path));
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
