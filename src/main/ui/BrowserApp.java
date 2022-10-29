package ui;

import model.html.ElementNode;
import model.html.HtmlParser;
import model.html.TextNode;
import model.util.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import persistance.JsonUtils;

import java.io.File;
import java.nio.file.*;
import java.util.*;

/**
 * The console interface to Apus.
 */
public class BrowserApp {
    private Scanner input;
    private static final String border = "===============================================";
    private static final String storagePath = "data/apus.cache";
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

        askToRestoreTabs();
        mainLoop();
    }

    /**
     * EFFECTS: Asks the user if they'd like to restore previously closed tabs.
     */
    private void askToRestoreTabs() {
        if (new File(storagePath).length() > 2) {
            println("Would you like to restore your previously closed tabs? (Y/N)");
            String answer;
            while (true) {
                answer = this.input.next();
                if (answer.equalsIgnoreCase("y")) {
                    restoreClosedTabs();
                    break;
                } else if (answer.equalsIgnoreCase("n")) {
                    JsonUtils.writeToFile(new JSONArray(), storagePath);
                    println("please provide a path to a file (examples located in data/*):");
                    pathString = this.input.next();
                    break;
                } else {
                    println("Sorry, I didn't quite get that. Please try again.");
                }
            }
        } else {
            println("please provide a path to a file (examples located in data/*):");
            pathString = this.input.next();
        }
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
     * EFFECTS: restores previous closed tabs from a cache file.
     */
    private void restoreClosedTabs() {
        try {
            JSONArray state = JsonUtils.readFromFile(storagePath);
            for (int i = 0; i < state.length(); i++) {
                println(state.get(i).getClass().getName());
                tabs.add((String) state.get(i));
            }
            pathString = tabs.removeLast();
        } catch (Exception e) {
            println("Restoring state from disk failed with " + e.toString());
            System.exit(0);
        }
    }

    /*
    private void mainLoopII(ArrayList<String> rawHtml, String border, HtmlParser parser) {
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
    }*/

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
                handleQuit();
                System.exit(0);
                break;
            default:
                println("Sorry, I didn't quite get that. Please try again.");
                break;
        }
    }

    /**
     * Helper function for the quit() case.
     * EFFECTS: Asks a user whether they'd like to save their tabs, and exists the program.
     */
    private void handleQuit() {
        println("Would you like to save your currently opened tabs to disk? (Y/N)");
        String answer;
        while (true) {
            answer = this.input.next();
            if (answer.equalsIgnoreCase("y")) {
                this.tabs.add(pathString);
                JsonUtils.writeToFile(new JSONArray(tabs), storagePath);
                break;
            } else if (answer.equalsIgnoreCase("n")) {
                JsonUtils.writeToFile(new JSONArray(), storagePath);
                break;
            } else {
                println("Sorry, I didn't quite get that. Please try again.");
            }
        }
    }

    /**
     * EFFECTS: writes the current program configuration to the disk
     */
    private void writeToDisk() {
        ArrayList<ArrayList<JSONObject>> jsonArray = new ArrayList<>();
        for (String p : tabs) {
            ArrayList<JSONObject> jsonArrayII = new ArrayList<>();
            try {
                Path path = Paths.get(pathString);
                String file = new String(Files.readAllBytes(path));
                HtmlParser parser = new HtmlParser();
                for (Node n : parser.parseHtml(file)) {
                    jsonArrayII.add(n.serialize());
                }
            } catch (Exception e) {
                System.out.printf("Failed to write to disk with %s", e);
            }
            jsonArray.add(jsonArrayII);
        }
        JsonUtils.writeToFile(new JSONArray(jsonArray), storagePath);
    }

    /**
     * EFFECTS: restores program state from a last written to state
     */
    private void restoreFromDisk(JSONArray state) {
        for (int i = 0; i < state.length(); i++) {
            Object tab = state.get(i);
            if (tab instanceof JSONArray) {
                for (int j = 0; j < ((JSONArray) tab).length(); j++) {
                    tabs.add(((JSONArray) tab).toString());
                }
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
