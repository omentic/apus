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
        var expected = new ArrayList<Node>();
        var expectedChildren = new ArrayList<Node>();
        var expectedGrandChildren = new ArrayList<Node>();
        var expectedGreatGrandChildren = new ArrayList<Node>();
        expected.add(new ElementNode("html", new ArrayList<>(), expectedChildren));
        expectedChildren.add(new ElementNode("head"));
        expectedChildren.add(new ElementNode("body", new ArrayList<>(), expectedGrandChildren));
        expectedGrandChildren.add(new ElementNode("p", new ArrayList<>(), expectedGreatGrandChildren));
        expectedGreatGrandChildren.add(new TextNode("Hello, world!"));

        var parser = new HtmlParser();
        assertEqualsHtml(parser.parseHtml(idiomaticHtml), expected);
    }

    @Test
    void testBrokenHtml() {
        var expected = new ArrayList<Node>();
        var expectedChildren = new ArrayList<Node>();
        var expectedGrandChildren = new ArrayList<Node>();
        expected.add(new ElementNode("html", new ArrayList<>(), expectedChildren));
        expectedChildren.add(new ElementNode("foo", new ArrayList<>(), expectedGrandChildren));
        expectedGrandChildren.add(new ElementNode("bar", new ArrayList<>()));
        expectedGrandChildren.add(new TextNode("<>"));

        var parser = new HtmlParser();
        assertEqualsHtml(parser.parseHtml(brokenHtml), expected);
    }

    @Test
    void testTrailingTextHtml() {
        var expected = new ArrayList<Node>();
        var expectedChildren = new ArrayList<Node>();
        var expectedGrandChildren = new ArrayList<Node>();
        expected.add(new TextNode("bot"));
        expected.add(new ElementNode("html", new ArrayList<>(), expectedChildren));
        expected.add(new TextNode("ba"));
        expectedChildren.add(new ElementNode("foo", new ArrayList<>(), expectedGrandChildren));
        expectedGrandChildren.add(new ElementNode("bar", new ArrayList<>()));

        var parser = new HtmlParser();
        assertEqualsHtml(parser.parseHtml(trailingTextHtml), expected);
    }

    @Test
    void testAttributesHtml() {
        var expected = new ArrayList<Node>();
        var expectedChildren = new ArrayList<Node>();
        var expectedAttributes = new ArrayList<Pair<String, String>>();
        expected.add(new ElementNode("html", new ArrayList<>(), expectedChildren));
        expectedChildren.add(new ElementNode("attr", expectedAttributes));
        expectedAttributes.add(new Pair<>("hello", "world"));
        expectedAttributes.add(new Pair<>("foo", "bar"));
        expectedAttributes.add(new Pair<>("strange", "cha\"rm"));
        expectedAttributes.add(new Pair<>("up", "do'wn"));

        var parser = new HtmlParser();
        var parsed = parser.parseHtml(attributesHtml);
        displayHtmlTree(parsed);
        assertEqualsHtml(parsed, expected);
    }

    /**
     * Complicated helper function for tests.
     */
    private static void assertEqualsHtml(ArrayList<Node> html, ArrayList<Node> expected) {
        for (int i = 0; i < html.size(); i++) {
            assertEquals(html.get(i).data(), expected.get(i).data());
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
