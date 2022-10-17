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

/**
 * This class assumes that it is getting _valid CSS_: that is, the style between two tags
 * of a style block, or the raw content of a .css file.
 * Making sure this assumption holds is extremely important for program robustness.
 * We do not check for validity, i.e. throw any exceptions - the driving principle of web standards is to "fail softly".
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

    /**
     * Parses a (valid) CSS file in a left-to-right, leftmost-derivation style.
     * It should be fast - I'd say something about time complexity if I knew anything about time complexity.
     * No guarantees are made about invalid CSS files. Also, no guarantees are made about valid CSS files, lol.
     */
    public static ArrayList<Pair<String, ArrayList<Pair<String, String>>>> parseLL(String input) {

        // parser buffers
        // essentially the CssTree type
        var result = new ArrayList<Pair<String, ArrayList<Pair<String, String>>>>();
        var currentSelector = "";
        var currentRule = new ArrayList<Pair<String, String>>();
        var currentProperty = "";
        var currentValue = "";
        var previousChar = '\0';

        // We safely assume to start by reading a selector.
        ParserState state = ParserState.SELECTORS;

        for (char c : input.toCharArray()) {
            // System.out.print(state);
            // System.out.println(" " + c);
            switch (state) {
                case SELECTORS:
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
                    break;
                case MEDIA_SELECTORS:
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
                    break;
                case ATTRIBUTE:
                    switch (c) {
                        case ':':
                            state = ParserState.VALUE;
                            break;
                        case '}':
                            state = ParserState.SELECTORS;
                            if (!currentValue.equals("") || !currentProperty.equals("")) {
                                System.out.println("something's wrong");
                                currentProperty = "";
                                currentValue = "";
                            }
                            result.add(new Pair<>(currentSelector, currentRule));
                            System.out.println(currentRule);
                            currentSelector = "";
                            currentRule = new ArrayList<>();
                            break;
                        case ' ': case '\n':
                            break;
                        default:
                            currentProperty += c;
                            break;
                    }
                    break;
                case VALUE:
                    switch (c) {
                        case ';':
                            state = ParserState.ATTRIBUTE;
                            currentRule.add(new Pair<>(currentProperty, currentValue));
                            currentProperty = "";
                            currentValue = "";
                            break;
                        case '}':
                            state = ParserState.SELECTORS;
                            if (!currentValue.equals("") || !currentProperty.equals("")) {
                                currentRule.add(new Pair<>(currentProperty, currentValue));
                                currentProperty = "";
                                currentValue = "";
                            }
                            result.add(new Pair<>(currentSelector, currentRule));
                            currentSelector = "";
                            currentRule = new ArrayList<>();
                            break;
                        case '\'':
                            state = ParserState.SINGLE_QUOTES;
                            currentValue += c;
                            break;
                        case '\"':
                            state = ParserState.DOUBLE_QUOTES;
                            currentValue += c;
                            break;
                        case ' ': case '\n':
                            break;
                        default:
                            currentValue += c;
                            break;
                    }
                    break;
                // quotes in css are exclusively? for paths: so we want to include the quotes themselves
                case SINGLE_QUOTES:
                    switch (c) {
                        case '\'':
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
                    break;
                case DOUBLE_QUOTES:
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
                    break;
            }
        }
        return result;
    }

    /**
     * Takes an input string with units and returns out the value in pixels.
     * This is a fault-tolerant system.
     * When given an invalid string (i.e. "12p53x"), it will produce an invalid result instead of throwing.
     * However, it should parse every valid string correctly.
     */
    private double parseUnits(String input) {
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
            System.out.printf("Did not parse a float from %s, proceeding with value 0.0...%n", numbers);
            value = 0.0;
        }
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
                System.out.printf("Unit %s not implemented, defaulting to %s in pixels...%n", units, value);
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
