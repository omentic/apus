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
    private static final String border = "===============================================";

    /**
     * EFFECTS: Renders an arbitrary HTML page and arbitrary HTML input.
     */
    public BrowserApp() {
        println("apus: currently a barebones html/css renderer");
        println("please provide a path to a file (examples located in data/*):");

        input = new Scanner(System.in);
        String pathString = input.next();
        Path path = Paths.get(pathString);
        try {
            String file = new String(Files.readAllBytes(path));
            HtmlParser parser = new HtmlParser();
            println(border);
            renderHtml(parser.parseHtml(file));
            println(border);
            ArrayList<String> rawHtml = new ArrayList<>();
            rawHtml.add(file);
            mainLoop(rawHtml, border, parser);
        } catch (Exception e) {
            println("Reading from the file failed with " + e.toString());
            println("Please try again.");
        }
    }


    /**
     * EFFECTS: Runs the main loop
     */
    private void mainLoop(ArrayList<String> rawHtml, String border, HtmlParser parser) {
        while (true) {
            println("Page rendered. Input additional raw HTML if desired.");
            rawHtml.add(input.next());
            println(border);
            for (String s : rawHtml) {
                parser = new HtmlParser();
                renderHtml(parser.parseHtml(s));
            }
            println(border);
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
