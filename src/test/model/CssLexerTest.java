package model;

import model.css.CssLexer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CssLexerTest {

    @Test
    void testIdiomaticHtml() {
        try {
            String idiomaticCss = Files.readString(Path.of("data/example.css"));
            String[] expected = {"body", "{", "background-color", ":", "#f0f0f2", ";", "margin", ":", "0", ";", "padding", ":", "0", ";", "font-family", ":", "-apple-system,", "system-ui,", "BlinkMacSystemFont,", "\"Segoe UI\",", "\"Open Sans\",", "\"Helvetica Neue\",", "Helvetica,", "Arial,", "sans-serif", ";", "}", "div", "{", "width", ":", "600px", ";", "margin", ":", "5em", "auto", ";", "padding", ":", "2em", ";", "background-color", ":", "#fdfdff", ";", "border-radius", ":", "0.5em", ";", "box-shadow", ":", "2px", "3px", "7px", "2px", "rgba(0,0,0,0.02)", ";", "}", "a", ":", "link,", "a", ":", "visited", "{", "color", ":", "#38488f", ";", "text-decoration", ":", "none", ";", "}", "@media", "(max-width", ":", "700px)", "{", "div", "{", "margin", ":", "0", "auto", ";", "width", ":", "auto", ";", "}", "}"};

            assertEquals(CssLexer.lex(idiomaticCss), Arrays.asList(expected));
            for (String i : CssLexer.lex(idiomaticCss)) {
                System.out.print("\"");
                System.out.print(i);
                System.out.print("\", ");
            }
        } catch (IOException e) {
            System.out.printf("fuck %s\n", e.toString());
            System.out.println(System.getProperty("user.dir"));
        }
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