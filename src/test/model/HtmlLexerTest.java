package model;

import model.html.HtmlLexer;

import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HtmlLexerTest {
    String idiomaticHtml = "<!DOCTYPE html><html><head></head><body><p>Hello,world!</p></body></html>";
    String brokenHtml = "<html><foo><bar></bar><ba";
    String trailingTextHtml = "<html><foo><bar></bar>ba";

    @Test
    void testIdiomaticHtml() {
        String[] idiomaticHtmlArray = {"<!DOCTYPE html>","<html>","<head>","</head>","<body>","<p>","Hello,world!","</p>","</body>","</html>"};
        assertEquals(HtmlLexer.lex(idiomaticHtml), Arrays.asList(idiomaticHtmlArray));
    }

    @Test
    void testBrokenHtml() {
        String[] brokenHtmlArray = {"<html>","<foo>","<bar>","</bar>","<ba>"};
        assertEquals(HtmlLexer.lex(brokenHtml), Arrays.asList(brokenHtmlArray));
    }

    @Test
    void testTrailingTextHtml() {
        String[] trailingTextHtmlArray = {"<html>","<foo>","<bar>","</bar>","ba"};
        assertEquals(HtmlLexer.lex(trailingTextHtml), Arrays.asList(trailingTextHtmlArray));
    }

/**
    FoodServicesCard c1;
    FoodServicesCard c2;
    FoodServicesCard c3;

    @BeforeEach
    void runBefore() {
        c1 = new FoodServicesCard(0);
        c2 = new FoodServicesCard(100);
        c3 = new FoodServicesCard(2000);
    }

    @Test
    void testReloadingAndPurchasing() {
        assertFalse(c1.makePurchase(100));
        assertEquals(c1.getBalance(), 0);
        c2.reload(10);
        assertEquals(c2.getBalance(), 110);
        assertTrue(c3.makePurchase(1400));
        assertEquals(c3.getBalance(), 600);
    }

    @Test
    void testRewardPoints() {
        if (c1.makePurchase(c1.POINTS_NEEDED_FOR_CASH_BACK / 2)) {
            assertEquals(c1.getRewardPoints(), (c1.POINTS_NEEDED_FOR_CASH_BACK / 2));
        } else {
            assertEquals(c1.getRewardPoints(), 0);
        }
        c2.makePurchase(c2.POINTS_NEEDED_FOR_CASH_BACK);
        assertEquals(c2.getRewardPoints(), 0);
        c3.makePurchase(1200);
        assertEquals(c3.getRewardPoints(), 1200 % c3.POINTS_NEEDED_FOR_CASH_BACK);
    }
    */
}