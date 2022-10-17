package model;

import model.html.HtmlParser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class HtmlParserTest {

    String idiomaticHtml = "<!DOCTYPE html><html><head></head><body><p>Hello,world!</p></body></html>";
    String brokenHtml = "<html><foo><bar></bar><ba";
    String trailingTextHtml = "<html><foo><bar></bar>ba";

    @Test
    void testIdiomaticHtml() {
        String[] idiomaticHtmlArray = {"<!DOCTYPE html>","<html>","<head>","</head>","<body>","<p>","Hello,world!","</p>","</body>","</html>"};
        System.out.println(HtmlParser.parseHtmlLL(idiomaticHtml));
//        assertEquals(HtmlParser.parseHtmlLL(idiomaticHtml), Arrays.asList(idiomaticHtmlArray));
    }

    @Test
    void testBrokenHtml() {
        String[] brokenHtmlArray = {"<html>","<foo>","<bar>","</bar>","<ba>"};
//        assertEquals(HtmlParser.parseHtmlLL(brokenHtml), Arrays.asList(brokenHtmlArray));
    }

    @Test
    void testTrailingTextHtml() {
        String[] trailingTextHtmlArray = {"<html>","<foo>","<bar>","</bar>","ba"};
//        assertEquals(HtmlParser.parseHtmlLL(trailingTextHtml), Arrays.asList(trailingTextHtmlArray));
    }
}
