package model.html;

import java.util.*;

import org.javatuples.*;
import org.json.JSONObject;
import persistance.JsonAble;

/**
 * This class represents the state of and implements an LL(1) HTML parser.
 * For convenience, the following (defo wrong) context-free grammar for HTML is below.
 * <br>
 * HTML ::= '<!DOCTYPE html>' (NODE)*
 * NODE ::= '<'TAG (' ' WORD '=' ('"'TEXT'"' | TEXT))* '>' (NODE)* '<\/' TAG '>'
 *         | '<'SELF_CLOSING_TAG (' ' WORD '=' ('"'TEXT'"' | TEXT))* ('>'|'/>')
 *         | (TEXT | NODE)*
 * TEXT ::= UNICODE - {'"'} + {'\"'}
 * TAG ::= 'body' | 'div' | ...
 * SELF_CLOSING_TAG ::= 'img' | ...
 * (note that \forall T \in SELF_CLOSING_TAG, T \notin TAG)
 */
public class HtmlParser implements JsonAble {

    /**
     * HTML is not nice to parse. We manage to get away with a relatively small number of parser states regardless.
     */
    private enum ParserState {
        HTML, IGNORED,
        OPENING_TAG, KEY, VALUE, // TAG::OPENING_TAG, TAG::KEY, TAG::VALUE
        SINGLE_QUOTES, DOUBLE_QUOTES, // VALUE::SINGLE_QUOTES, VALUE::DOUBLE_QUOTES
        UNKNOWN_TAG, CLOSING_TAG,
    }

    // HTML documents are uniquely a list of Nodes rather than a Node themselves
    private ArrayList<Node> result;
    // a bunch of useful buffers. see CssParser for commentary.
    private ArrayDeque<ElementNode> unfinished;
    private String currentTag;
    private ArrayList<Pair<String, String>> currentAttributes;
    private String currentKey;
    private String currentValue;
    private String currentText;
    // important for quote escapes, and multiple whitespace chars
    private char previousChar;

    private ParserState state;

    public HtmlParser() {
        result = new ArrayList<>();
        unfinished = new ArrayDeque<>();
        currentTag = "";
        currentAttributes = new ArrayList<>();
        currentKey = "";
        currentValue = "";
        currentText = "";
        previousChar = '\0';

        // We safely? assume to start outside of all nodes.
        state = ParserState.HTML;
    }

    public ArrayList<Node> parseHtml(String input) {

        for (char c : input.toCharArray()) {
            // System.out.print(state);
            // System.out.println(" " + c + " " + currentText);
            switch (state) {
                case HTML: caseHtml(c);
                    break;
                case UNKNOWN_TAG: caseUnknownTag(c);
                    break; // FOOTGUN LANGUAGE DESIGN STRIKES AGAIN
                case IGNORED: caseIgnored(c);
                    break;
                case OPENING_TAG: caseOpeningTag(c);
                    break;
                case CLOSING_TAG: caseClosingTag(c);
                    break;
                case KEY: caseKey(c);
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
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the HTML state.
     * MODIFIES: this
     */
    private void caseHtml(char c) {
        switch (c) {
            case '<':
                state = ParserState.UNKNOWN_TAG;
                if (!currentText.equals("")) {
                    addNewTextNode();
                }
                break; // FOOTGUN LANGUAGE DESIGN
            case ' ': case '\n':
                if (previousChar != ' ') {
                    currentText += ' ';
                }
                previousChar = ' ';
                break;
            default:
                currentText += c;
                previousChar = c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the UNKNOWN_TAG state.
     * MODIFIES: this
     */
    private void caseUnknownTag(char c) {
        switch (c) {
            case '/':
                state = ParserState.CLOSING_TAG;
                break;
            case '>': // Why would you put <> in your HTML??? go away
                state = ParserState.HTML;
                currentText += "<>";
                break;
            // For now, we'll straight-up ignore anything matching the <!...> syntax:
            // i.e. comments, and <!DOCTYPE html>
            case '!':
                state = ParserState.IGNORED;
                break;
            default:
                state = ParserState.OPENING_TAG;
                currentTag += c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the IGNORED state.
     * MODIFIES: this
     */
    private void caseIgnored(char c) {
        switch (c) {
            case '>':
                state = ParserState.HTML;
                break;
            default:
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the OPENING_TAG state.
     * MODIFIES: this
     */
    private void caseOpeningTag(char c) {
        switch (c) {
            case '>':
                addNewElementNode();
                break;
            case ' ': case '\n':
                state = ParserState.KEY;
                break;
            default:
                currentTag += c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the CLOSING_TAG state.
     * MODIFIES: this
     */
    private void caseClosingTag(char c) {
        switch (c) {
            case '>':
                state = ParserState.HTML;
                // IMPORTANT: we don't validate that closing tags correspond to an open tag
                if (!isSelfClosingTag(currentTag)) {
                    if (unfinished.size() != 0) {
                        unfinished.removeLast();
                    }
                }
                currentTag = "";
                break;
            case ' ': case '\n':
                break;
            default:
                currentTag += c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the KEY state.
     * MODIFIES: this
     */
    private void caseKey(char c) {
        switch (c) {
            case '>':
                addNewElementNode();
                break;
            case '=':
                state = ParserState.VALUE;
                break;
            case ' ': case '\n':
                break;
            default:
                currentKey += c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the VALUE state.
     * MODIFIES: this
     */
    private void caseValue(char c) {
        switch (c) {
            case '\'': state = ParserState.SINGLE_QUOTES;
                break;
            case '\"': state = ParserState.DOUBLE_QUOTES;
                break;
            case ' ': case '\n':
                state = ParserState.KEY;
                currentAttributes.add(new Pair<>(currentKey, currentValue));
                currentKey = "";
                currentValue = "";
                break; // THE FOOTGUN DESIGN STRIKES AGAIN
            case '>':
                if (!currentKey.equals("") || !currentValue.equals("")) {
                    currentAttributes.add(new Pair<>(currentKey, currentValue));
                    currentKey = "";
                    currentValue = "";
                }
                addNewElementNode();
                break;
            default:
                currentValue += c;
                break;
        }
    }

    /**
     * EFFECTS: Handles and updates parser state/buffers for a single character while in the SINGLE_QUOTES state.
     * MODIFIES: this
     */
    private void caseSingleQuotes(char c) {
        switch (c) {
            case '\'':
                if (previousChar != '\\') {
                    state = ParserState.VALUE;
                    previousChar = '\0';
                } else {
                    currentValue = currentValue.substring(0, currentValue.length() - 1);
                    currentValue += c;
                    previousChar = c;
                }
                break;
            default:
                currentValue += c;
                previousChar = c;
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
                    previousChar = '\0';
                } else {
                    currentValue = currentValue.substring(0, currentValue.length() - 1);
                    currentValue += c;
                    previousChar = c;
                }
                break; // FOOTGUN LANGUAGE DESIGN
            default:
                currentValue += c;
                previousChar = c;
                break;
        }
    }

    /**
     * Helper function to remove code duplication.
     * EFFECTS: Creates and adds a new ElementNode from the current buffers to the unfinished and result stacks
     * MODIFIES: this
     */
    private void addNewElementNode() {
        state = ParserState.HTML;
        ElementNode node = new ElementNode(currentTag, currentAttributes);
        if (unfinished.size() != 0) {
            unfinished.getLast().addChild(node);
            if (!isSelfClosingTag(currentTag)) {
                unfinished.add(node);
            }
        } else {
            result.add(node);
            if (!isSelfClosingTag(currentTag)) {
                unfinished.add((ElementNode) result.get(result.size() - 1));
            }
        }
        currentTag = "";
        currentAttributes = new ArrayList<>();
    }

    /**
     * Helper function to check method length boxes.
     * EFFECTS: Creates and adds a new TextNode from the current buffers to the unfinished and result stacks
     * MODIFIES: this
     */
    private void addNewTextNode() {
        if (unfinished.size() != 0) {
            unfinished.getLast().addChild(new TextNode(currentText));
        } else {
            result.add(new TextNode(currentText));
        }
        currentText = "";
        previousChar = '\0';
    }

    /**
     * Simple helper function to check if a tag is self-closing.
     * EFFECTS: Returns whether a String tag is a self-closing tag.
     */
    private static boolean isSelfClosingTag(String tag) {
        switch (tag) {
            case "input": case "param":
            case "br": case "hr": case "wbr":
            case "img": case "embed": case "area":
            case "meta":  case "base": case "link":
            case "source": case "track": case "col":
                return true;
            default:
                return false;
        }
    }

    public JSONObject serialize() {
        return new JSONObject(this);
    }
}

/*
<!DOCTYPE html>
<html>
<head>
	<title>j-james</title>
	<meta charset="utf-8"/>
	<meta name="viewport" content="width=device-width"/>
	<link rel="icon" type="image/jpg" href="assets/compass.jpg"/>
	<link rel="stylesheet" href="css/normalize.css"/>
	<link rel="stylesheet" href="css/style.css"/>
</head>
<body>
	<header>
		<h1>
			<a href="https://j-james.me">j-james</a>
		</h1>
		<nav>
			<a href="https://j-james.me/about">about</a>
			<a href="https://j-james.me/resume">resume</a>
			<a href="https://j-james.me/posts">posts</a>
			<a href="https://j-james.me/writeups">writeups</a>
		</nav>
	</header>
	<main>
	<div id="intro">
		<img id="face" src="assets/compass.jpg"/>
	</div>
	<div id="details">
		<h2>Projects</h2>
		<p> Lorem ipsum dolor sit amet, consectetur adipiscing elit,
		sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
		Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris
		nisi ut aliquip ex ea commodo consequat.
		Duis aute irure dolor in reprehenderit in voluptate velit esse cillum
		dolore eu fugiat nulla pariatur.
		Excepteur sint occaecat cupidatat non proident, sunt in culpa
		qui officia deserunt mollit anim id est laborum. </p>
		<!-- <h2>Posts</h2>
		<p> Lorem ipsum dolor sit amet, consectetur adipiscing elit,
		sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
		Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris
		nisi ut aliquip ex ea commodo consequat.
		Duis aute irure dolor in reprehenderit in voluptate velit esse cillum
		dolore eu fugiat nulla pariatur.
		Excepteur sint occaecat cupidatat non proident, sunt in culpa
		qui officia deserunt mollit anim id est laborum. </p> -->
	</div>
	</main>
	<footer>
		<span><img src="assets/copyleft.svg" width="12" height="12"/> 2020-2022 j-james </span>
	</footer>
</body>
</html>
*/
