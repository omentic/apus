package ui;

import model.html.ElementNode;
import model.html.HtmlParser;
import model.html.TextNode;
import model.util.Node;

import java.nio.file.*;
import java.util.*;

/**
 * The console interface to Apus.
 */
public class BrowserApp {
    private Scanner input;

    /**
     * EFFECTS: Renders an arbitrary HTML page and arbitrary HTML input.
     */
    public BrowserApp() {
        println("apus: currently a barebones html/css renderer");
        println("please provide a path to a file (examples located in data/*):");

        String pathString = input.next();
        Path path = Path.of(pathString);
        try {
            String file = Files.readString(path);
            HtmlParser parser = new HtmlParser();
            renderHtml(parser.parseHtml(file));
            println("Page rendered. Input raw HTML to add Nodes.");
            parser = new HtmlParser();
            String rawHtml = input.next();
            renderHtml(parser.parseHtml(file + rawHtml));
        } catch (Exception e) {
            println("Reading from the file failed with " + e.toString());
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

    private void print(String toPrint) {
        System.out.print(toPrint);
    }

    private void println(String toPrint) {
        System.out.println(toPrint);
    }

}
