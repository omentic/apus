package ui;

import model.BrowserState;
import model.html.HtmlParser;
import model.util.Event;
import model.util.EventLog;
import org.json.JSONArray;
import persistance.JsonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;

// Broad JFrame usage taken from here: https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
public class BrowserWindow extends JFrame {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    private static final String storagePath = "data/apus.cache";

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
//        browserBar.addTab("/home/apropos/Projects/website/j-james/index.html");
        setVisible(true);
        setClosingBehavior();

        initializeBrowser();
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

    // EFFECTS: Prompts the user to save their tabs before quitting
    private void setClosingBehavior() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (state.getTabs().size() > 0) {
                    saveCurrentTabs();
                }
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event);
                }
                super.windowClosing(e);
            }
        });
    }

    // EFFECTS: sets up the browser upon launching
    private void initializeBrowser() {
        if (new File(storagePath).length() > 2) {
            restorePreviousTabs();
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts the user to restore their previous tabs
    private void restorePreviousTabs() {
        int answer = JOptionPane.showOptionDialog(
                this, "Would you like to restore your previous tabs?", "apus",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Yes", "No"}, "Yes");
        if (answer == 0) {
            try {
                JSONArray state = JsonUtils.readFromFile(storagePath);
                for (int i = 0; i < state.length(); i++) {
                    this.browserBar.addTab((String) state.get(i));
                    this.state.addTab((String) state.get(i));
                }
            } catch (Exception e) {
                System.out.println("Restoring state from disk failed with " + e.toString());
            }
        }
    }

    // EFFECTS: prompts the user to save their current tabs before closing
    private void saveCurrentTabs() {
        int answer = JOptionPane.showOptionDialog(
                this, "Would you like to save your current tabs?", "apus",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Yes", "No"}, "Yes");
        if (answer == 0) {
            JsonUtils.writeToFile(new JSONArray(state.getTabs()), storagePath);
        } else {
            JsonUtils.writeToFile(new JSONArray(), storagePath);
        }
    }

    public BrowserState getBrowserState() {
        return this.state;
    }
}
