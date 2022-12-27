package ui;

import model.html.ElementNode;
import model.html.HtmlParser;
import model.html.TextNode;
import model.html.Node;

import java.nio.file.*;
import java.util.*;

/**
 * The console interface to Apus.
 */
public class BrowserApp {
    private Scanner input;
    private static final String border = "===============================================";
    private String pathString;
    private ArrayList<Node> parsed;
    private ArrayDeque<String> tabs;

    /**
     * EFFECTS: Renders an arbitrary HTML page and arbitrary HTML input.
     */
    public BrowserApp() {
        println("apus: currently a barebones html/css renderer");
        this.input = new Scanner(System.in);
        this.tabs = new ArrayDeque<>();
        mainLoop();
    }

    /**
     * EFFECTS: Runs the main loop
     */
    private void mainLoop() {
        while (true) {
            try {
                Path path = Paths.get(pathString);
                String file = new String(Files.readAllBytes(path));
                HtmlParser parser = new HtmlParser();
                parsed = parser.parseHtml(file);
                println(border);
                renderHtml(parsed);
                println(border);
                println("Page rendered. Input additional commands if desired.");
                println("Impemented commands: newuri, newtab, nexttab, quit");
                handleInput(this.input.next());
                println(border);
            } catch (Exception e) {
                println("Reading from the file failed with " + e.toString());
                println("Please try again.");
            }
        }
    }

    /**
     * EFFECTS: Barebones HTML rendering. Iterates through a list of Nodes and their children and prints any text.
     */
    private void renderHtml(ArrayList<Node> html) {
        for (Node node: html) {
            if (node instanceof TextNode) {
                println(node.getData());
            } else {
                renderHtml(((ElementNode) node).getChildren());
            }
        }
    }

    /**
     * EFFECTS: Handles user input after rendering an initial site
     */
    private void handleInput(String input) {
        switch (input) {
            case "newuri":
                println("please provide a path to a file (examples located in data/*):");
                pathString = this.input.next();
                break;
            case "newtab":
                this.tabs.add(pathString);
                println("please provide a path to a file (examples located in data/*):");
                pathString = this.input.next();
                break;
            case "nexttab":
                this.tabs.add(pathString);
                pathString = this.tabs.removeFirst();
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                println("Sorry, I didn't quite get that. Please try again.");
                break;
        }
    }

    private void print(String toPrint) {
        System.out.print(toPrint);
    }

    private void println(String toPrint) {
        System.out.println(toPrint);
    }

}
