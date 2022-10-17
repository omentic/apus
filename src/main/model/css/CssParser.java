package model.css;

import org.javatuples.*;

import java.util.*;

/*
 * RULES ::= (RULE)+
 * RULE ::= SELECTORS '{' (PROPERTY | (PROPERTY ';')*) '}'
 * SELECTORS ::= SELECTOR (COMBINATOR SELECTOR)*
 * SELECTOR ::= TAG | '#' WORD | '.' WORD
 * COMBINATOR ::= '<' | '*' | '~' | ' ' | ...
 * PROPERTY ::= ATTRIBUTE ':' VALUE
 * ATTRIBUTE ::= 'color' | 'text' | ...
 * VALUE ::= ??? idk lol
 */
public class CssParser {

    /**
     * CSS is nice to parse, and so we have a relatively small number of parser states.
     */
    private enum ParserState {
        SELECTORS, MEDIA_SELECTORS,
        ATTRIBUTE, VALUE, // PROPERTIES::PROPERTY::ATTRIBUTE, PROPRETIES::PROPERTY::VALUE
        SINGLE_QUOTES, DOUBLE_QUOTES, // VALUE::SINGLE_QUOTES, VALUE::DOUBLE_QUOTES
    }

    // essentially the csstree type, only we don't need it to be a tree
    private ArrayList<Pair<String, ArrayList<Pair<String, String>>>> result;
    // a bunch of useful buffers: optimizations in the future could likely come from tweaking these
    // note that i know nothing about data structure performance: but i'm pretty sure that Strings
    // are _not_ the right tool for the job here, lol
    private String currentSelector;
    private ArrayList<Pair<String, String>> currentRule;
    private String currentProperty;
    private String currentValue;
    // important for quote escapes
    private char previousChar;

    private ParserState state;

    /// Initialize all buffers to default values
    public CssParser() {
        result = new ArrayList<>();
        currentSelector = "";
        currentRule = new ArrayList<>();
        currentProperty = "";
        currentValue = "";
        previousChar = '\0';

        // We safely assume to start by reading a selector.
        state = ParserState.SELECTORS;
    }

    /**
     * Parses a (valid) CSS file in a left-to-right, leftmost-derivation style. No additional lookup is needed,
     * however we do keep a previousChar value for dealing with (annoying) escaped quotes.
     * It should be fast - I'd say something about time complexity if I knew anything about time complexity.
     * No guarantees are made about invalid CSS files. Also, no guarantees are made about valid CSS files, lol.
     * <br>
     * REQUIRES: A valid CSS file, as a raw String.
     * MODIFIES: this
     * EFFECTS: Returns a parsed CSS representation as several nested ArrayLists and Pairs of Strings.
     */
    public ArrayList<Pair<String, ArrayList<Pair<String, String>>>> parseCSS(String input) {

        for (char c : input.toCharArray()) {
            // System.out.print(state);
            // System.out.println(" " + c);
            switch (state) {
                case SELECTORS: caseSelectors(c);
                    break;
                case MEDIA_SELECTORS: caseMediaSelectors(c);
                    break;
                case ATTRIBUTE: caseAttribute(c);
                    break;
                case VALUE: caseValue(c);
                    break;
                case SINGLE_QUOTES: caseSingleQuotes(c);
                    break;
                case DOUBLE_QUOTES: caseDoubleQuotes(c);
                    break;
            }
        }
        return result;
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the SELECTORS state.
     * See also: the (slightly wrong) context-free grammar commented at the start of this file.
     * MODIFIES: this
     */
    private void caseSelectors(char c) {
        switch (c) {
            case '@':
                if (currentSelector.equals("")) {
                    state = ParserState.MEDIA_SELECTORS;
                } else {
                    currentSelector += c;
                }
                break;
            case '{':
                state = ParserState.ATTRIBUTE;
                break;
            case ' ': case '\n':
                break;
            // todo: do better than blindly create a string; pattern match on css selectors
            default:
                currentSelector += c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the MEDIA_SELECTORS state.
     * MODIFIES: this
     */
    private void caseMediaSelectors(char c) {
        switch (c) {
            // todo: don't entirely disregard media queries, also split between @media/@...
            case '{':
                state = ParserState.SELECTORS;
                // discard currentSelector
                currentSelector = "";
                break;
            default:
                currentSelector += c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the ATTRIBUTE state.
     * MODIFIES: this
     */
    private void caseAttribute(char c) {
        switch (c) {
            case ':':
                state = ParserState.VALUE;
                break;
            case '}':
                state = ParserState.SELECTORS;
                if (!currentValue.equals("") || !currentProperty.equals("")) {
                    // System.out.println("something's wrong");
                    currentProperty = "";
                    currentValue = "";
                }
                result.add(new Pair<>(currentSelector, currentRule));
                currentSelector = "";
                currentRule = new ArrayList<>();
                break;
            case ' ': case '\n':
                break;
            default:
                currentProperty += c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the VALUE state.
     * MODIFIES: this
     */
    private void caseValue(char c) {
        switch (c) {
            case ';':
                state = ParserState.ATTRIBUTE;
                updateCurrentRule();
                break;
            case '}':
                state = ParserState.SELECTORS;
                if (!currentValue.equals("") || !currentProperty.equals("")) {
                    updateCurrentRule();
                }
                result.add(new Pair<>(currentSelector, currentRule));
                currentSelector = "";
                currentRule = new ArrayList<>();
                break;
            // todo: handle spaces better: they're actually important inside values
            case ' ': case '\n': break; // believe me, i think this is ugly too but it passes checkstyle
            case '\'':
                state = ParserState.SINGLE_QUOTES;
                currentValue += c;
                break;
            // intentional use of TERRIBLE SMOKING FOOTGUN behavior to check boxes
            case '\"': state = ParserState.DOUBLE_QUOTES;
            default: currentValue += c;
                break;
        }
    }

    /**
     * Helper function to check method length boxes.
     * EFFECTS: Adds a new property to the current rule.
     * MODIFIES: this
     */
    private void updateCurrentRule() {
        currentRule.add(new Pair<>(currentProperty, currentValue));
        currentProperty = "";
        currentValue = "";
    }

    // todo: handle additional escaped characters, though what we have right now isn't bad

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the SINGLE_QUOTES state.
     * MODIFIES: this
     */
    private void caseSingleQuotes(char c) {
        switch (c) {
            case '\'':
                if (previousChar != '\\') {
                    state = ParserState.VALUE;
                    // quotes in css are exclusively? for paths: so we want to include the quotes themselves
                    currentValue += c;
                    previousChar = '\0';
                } else {
                    // possibly not the best way to handle this, may be better to keep the backslash
                    currentValue = currentValue.substring(0, currentValue.length() - 2);
                    currentValue += c;
                    previousChar = c;
                }
                break;
            default:
                currentValue += c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the DOUBLE_QUOTES state.
     * MODIFIES: this
     */
    private void caseDoubleQuotes(char c) {
        switch (c) {
            case '\"':
                if (previousChar != '\\') {
                    state = ParserState.VALUE;
                    currentValue += c;
                    previousChar = '\0';
                } else {
                    currentValue = currentValue.substring(0, currentValue.length() - 2);
                    currentValue += c;
                    previousChar = c;
                }
                break;
            default:
                currentValue += c;
                break;
        }
    }

    /**
     * Takes an input string with units and returns out the value in pixels. This is a fault-tolerant system.
     * When given an invalid string (i.e. "12p53x"), it will produce an invalid result instead of throwing.
     * However, it should parse every valid string correctly.
     * <br>
     * REQUIRES: A string of the form [NUMBER][VALIDUNIT]
     * EFFECTS: Returns a number, in pixels, that has been converted appropriately
     */
    private static double parseUnits(String input) {
        String numbers = "";
        String units = "";
        // imagine making a language without iterable strings, fml
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c) || c == '.' || c == '-') {
                numbers += c;
            } else {
                units += c;
            }
        }
        double value;
        try {
            value = Float.parseFloat(numbers);
        } catch (NumberFormatException e) {
            // System.out.printf("Did not parse a float from %s, proceeding with value 0.0...%n", numbers);
            value = 0.0;
        }
        return convertUnits(units, value);
    }

    /**
     * REQUIRES: a String that is a unit, otherwise defaults to pixels
     * EFFECTS: converts a value in some units to a value in pixels
     */
    private static double convertUnits(String units, double value) {
        // god case/break is such a fault-provoking design i hate it
        // good thing we avoid breaks entirely here lmao
        switch (units) {
            // absolute units
            case "px": return value;
            case "pc": return value * 16;
            case "pt": return value * (4.0 / 3.0);
            case "cm": return value * 37.8;
            case "mm": return value * 378;
            case "Q":  return value * 1512;
            case "in": return value * 96;
            // not handled: % em ex ch rem lh rlh vw vh vmin vmax vb vi svw svh lvw lvh dvw dvh
            default:
                // System.out.printf("Unit %s not implemented, defaulting to %s in pixels...%n", units, value);
                return value;
        }
    }
}

/*
 *     body {
 *         background-color: #f0f0f2;
 *         margin: 0;
 *         padding: 0;
 *         font-family: -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI",
 *         "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
 *
 *     }
 *     div {
 *         width: 600px;
 *         margin: 5em auto;
 *         padding: 2em;
 *         background-color: #fdfdff;
 *         border-radius: 0.5em;
 *         box-shadow: 2px 3px 7px 2px rgba(0,0,0,0.02);
 *     }
 *     a:link, a:visited {
 *         color: #38488f;
 *         text-decoration: none;
 *     }
 *     @media (max - width : 700px) {
 *         div {
 *             margin: 0 auto;
 *             width: auto;
 *         }
 *     }
 */
