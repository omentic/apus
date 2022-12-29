package model.css;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CssParserTest {

    String idiomaticCss = "body {    background-color: '#\\'f0f0f2';    margin: 0;  padding: 0;    font-family: -apple-system, system-ui, BlinkMacSystemFont, \"Segoe\\\" UI\",    'Open\\' Sans', \"Helvetica Neue\", Helvetica, Arial, sans-serif;}div {    width: 600px;    margin: 5em auto;    padding: 2em;    background-color: #fdfdff;    border-radius: 0.5em;    box-shadow: 2px 3px 7px 2px rgba(0,0,0,0.02);}a:link, a:visited {    color: #38488f;    text-decoration: none;}@media (max - width : 700px) {    @media () {div {        margin: 0 auto;        width: auto    }}}";

    @Test
    void testIdiomaticCss() {
        var expected = new ArrayList<Pair<String, ArrayList<Pair<String, String>>>>();
        var body = new ArrayList<Pair<String, String>>();
        var divOne = new ArrayList<Pair<String, String>>();
        var selectors = new ArrayList<Pair<String, String>>();
        var divTwo = new ArrayList<Pair<String, String>>();
        expected.add(new Pair<>("body", body));
        expected.add(new Pair<>("div", divOne));
        expected.add(new Pair<>("a:link,a:visited", selectors));
        expected.add(new Pair<>("div", divTwo));
        body.add(new Pair<>("background-color", "'#'f0f0f2'"));
        body.add(new Pair<>("margin", "0"));
        body.add(new Pair<>("padding", "0"));
        body.add(new Pair<>("font-family", "-apple-system,system-ui,BlinkMacSystemFont,\"Segoe\" UI\",'Open' Sans',\"Helvetica Neue\",Helvetica,Arial,sans-serif"));
        divOne.add(new Pair<>("width", "600px"));
        divOne.add(new Pair<>("margin", "5emauto"));
        divOne.add(new Pair<>("padding", "2em"));
        divOne.add(new Pair<>("background-color", "#fdfdff"));
        divOne.add(new Pair<>("border-radius", "0.5em"));
        divOne.add(new Pair<>("box-shadow", "2px3px7px2pxrgba(0,0,0,0.02)"));
        selectors.add(new Pair<>("color", "#38488f"));
        selectors.add(new Pair<>("text-decoration", "none"));
        divTwo.add(new Pair<>("margin", "0auto"));
        divTwo.add(new Pair<>("width", "auto"));

        CssParser parser = new CssParser();
        assertEqualsCss(parser.parseCSS(idiomaticCss), expected);
    }

    @Test
    void testConversions() {
        assertEquals(CssParser.parseUnits("0gosf"), 0);
        assertEquals(CssParser.parseUnits("0px"), 0);
        assertEquals(CssParser.parseUnits("0.0pc"), 0);
        assertEquals(CssParser.parseUnits("0.00pt"), 0);
        assertEquals(CssParser.parseUnits("0cm"), 0);
        assertEquals(CssParser.parseUnits("0mm"), 0);
        assertEquals(CssParser.parseUnits("0Q"), 0);
        assertEquals(CssParser.parseUnits("0in"), 0);
    }

    public static void assertEqualsCss(ArrayList<Pair<String, ArrayList<Pair<String, String>>>> css,
                                       ArrayList<Pair<String, ArrayList<Pair<String, String>>>> expected) {
        for (int i = 0; i < css.size(); i++) {
            assertEquals(css.get(i).getValue0(), expected.get(i).getValue0());
            for (int j = 0; j < css.get(i).getValue1().size(); j++) {
                assertEquals(css.get(i).getValue1().get(j).getValue0(), expected.get(i).getValue1().get(j).getValue0());
                assertEquals(css.get(i).getValue1().get(j).getValue1(), expected.get(i).getValue1().get(j).getValue1());
            }
        }
    }
}
