package ui;

import model.html.*;

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

    // Renders an arbitrary HTML page and arbitrary HTML input.
    public BrowserApp() {
        println("apus: currently a barebones html/css renderer");
        this.input = new Scanner(System.in);
        this.tabs = new ArrayDeque<>();

        while (true) {
            try {
                String file = Files.readString(Path.of(pathString));
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

    // Barebones HTML rendering. Iterates through a list of Nodes and their children and prints any text.
    private void renderHtml(ArrayList<Node> html) {
        for (Node node: html) {
            switch (node) {
                case ElementNode e -> {
                    renderHtml(e.getChildren());
                }
                default -> {
                    println(node.getData());
                }
            }
        }
    }

    // Handles user input after rendering an initial site
    private void handleInput(String input) {
        switch (input) {
            case "newuri" -> {
                println("please provide a path to a file (examples located in data/*):");
                pathString = this.input.next();
            }
            case "newtab" -> {
                this.tabs.add(pathString);
                println("please provide a path to a file (examples located in data/*):");
                pathString = this.input.next();
            }
            case "nexttab" -> {
                this.tabs.add(pathString);
                pathString = this.tabs.removeFirst();
            }
            case "quit" -> System.exit(0);
            default -> println("Sorry, I didn't quite get that. Please try again.");
        }
    }

    private void print(String toPrint) {
        System.out.print(toPrint);
    }

    private void println(String toPrint) {
        System.out.println(toPrint);
    }

}
