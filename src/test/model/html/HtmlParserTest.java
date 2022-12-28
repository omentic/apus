package model.html;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HtmlParserTest {

    String idiomaticHtml = "<!DOCTYPE html><html><head></head><body><p>Hello,  world!</p></body></html>";
    String brokenHtml = "<html><foo><bar></bar><><ba";
    String trailingTextHtml = "bot<html><foo><bar></bar>ba";
    String attributesHtml = "<html><attr hello=\"world\" foo='bar' strange=\"cha\\\"rm\" up='do\\'wn'></attr></html>";

    @Test
    void testIdiomaticHtml() {
        ArrayList<Node> expected = new ArrayList<>();
        ArrayList<Node> expectedChildren = new ArrayList<>();
        ArrayList<Node> expectedGrandChildren = new ArrayList<>();
        ArrayList<Node> expectedGreatGrandChildren = new ArrayList<>();
        expected.add(new ElementNode("html", new ArrayList<>(), expectedChildren));
        expectedChildren.add(new ElementNode("head"));
        expectedChildren.add(new ElementNode("body", new ArrayList<>(), expectedGrandChildren));
        expectedGrandChildren.add(new ElementNode("p", new ArrayList<>(), expectedGreatGrandChildren));
        expectedGreatGrandChildren.add(new TextNode("Hello, world!"));

        HtmlParser parser = new HtmlParser();
        assertEqualsHtml(parser.parseHtml(idiomaticHtml), expected);
        // displayHtmlTree(parser.parseHtml(idiomaticHtml));
    }

    @Test
    void testBrokenHtml() {
        ArrayList<Node> expected = new ArrayList<>();
        ArrayList<Node> expectedChildren = new ArrayList<>();
        ArrayList<Node> expectedGrandChildren = new ArrayList<>();
        expected.add(new ElementNode("html", new ArrayList<>(), expectedChildren));
        expectedChildren.add(new ElementNode("foo", new ArrayList<>(), expectedGrandChildren));
        expectedGrandChildren.add(new ElementNode("bar", new ArrayList<>()));
        expectedGrandChildren.add(new TextNode("<>"));

        HtmlParser parser = new HtmlParser();
        assertEqualsHtml(parser.parseHtml(brokenHtml), expected);
        // displayHtmlTree(parser.parseHtml(brokenHtml));
    }

    @Test
    void testTrailingTextHtml() {
        ArrayList<Node> expected = new ArrayList<>();
        ArrayList<Node> expectedChildren = new ArrayList<>();
        ArrayList<Node> expectedGrandChildren = new ArrayList<>();
        expected.add(new TextNode("bot"));
        expected.add(new ElementNode("html", new ArrayList<>(), expectedChildren));
        expected.add(new TextNode("ba"));
        expectedChildren.add(new ElementNode("foo", new ArrayList<>(), expectedGrandChildren));
        expectedGrandChildren.add(new ElementNode("bar", new ArrayList<>()));

        HtmlParser parser = new HtmlParser();
        assertEqualsHtml(parser.parseHtml(trailingTextHtml), expected);
        // displayHtmlTree(parser.parseHtml(trailingTextHtml));
    }

    @Test
    void testAttributesHtml() {
        ArrayList<Node> expected = new ArrayList<>();
        ArrayList<Node> expectedChildren = new ArrayList<>();
        ArrayList<Pair<String, String>> expectedAttributes = new ArrayList<>();
        expected.add(new ElementNode("html", new ArrayList<>(), expectedChildren));
        expectedChildren.add(new ElementNode("attr", expectedAttributes));
        expectedAttributes.add(new Pair<>("hello", "world"));
        expectedAttributes.add(new Pair<>("foo", "bar"));
        expectedAttributes.add(new Pair<>("strange", "cha\"rm"));
        expectedAttributes.add(new Pair<>("up", "do'wn"));

        HtmlParser parser = new HtmlParser();
        ArrayList<Node> parsed = parser.parseHtml(attributesHtml);
        displayHtmlTree(parsed);
        assertEqualsHtml(parsed, expected);
    }

    /**
     * Complicated helper function for tests.
     */
    private static void assertEqualsHtml(ArrayList<Node> html, ArrayList<Node> expected) {
        for (int i = 0; i < html.size(); i++) {
            assertEquals(html.get(i).data(), expected.get(i).data());
            // System.out.println(html.get(i).getData() + " " + expected.get(i).getData());
            switch (html.get(i)) {
                case ElementNode e ->
                        assertEqualsHtml(e.children, ((ElementNode) expected.get(i)).children);
                default -> {}
            }
        }
    }

    /**
     * Simple helper function for debugging.
     * EFFECTS: prints a representation of the tree to the console for debugging purposes
     */
    private void displayHtmlTree(ArrayList<Node> tree) {
        for (Node node : tree) {
            switch (node) {
                case ElementNode e -> {
                    System.out.print(e.tag + ": ");
                    for (Node n : e.children) {
                        System.out.print(n.data() + " ");
                    }
                    System.out.println();
                    displayHtmlTree(e.children);
                }
                default -> System.out.println("Text: " + node.data());
            }
        }
    }
}
