package model.html;

import java.util.*;

import model.util.Node;
import org.javatuples.*;

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
		<div id="profile">
			<p> Hello, I'm JJ, and I go by j-james on the Internet. </p>
			<p> I'm a second-year student at the <a href="https://ubc.ca">University of British Columbia</a>, flag hunter for <a href="https://ubcctf.github.io">Maple Bacon</a>, embedded programmer on <a href="https://ubcbionics.com/">UBC Bionics</a>, and occasional ultimate frisbee and roller/ice hockey player.</p>
			<p> Outside of school, sports, and social life, I enjoy building and contributing to <a href="https://www.gnu.org/philosophy/free-sw">free-and-open-source</a> projects. The majority of my work can either be found on <a href="https://github.com/j-james">GitHub</a> or at <a href="https://sr.ht/~j-james">SourceHut</a>. </p>
		</div>
	</div>
	<!-- <div id="details">
		<h2>Projects</h2>
		<p> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. </p>
		<h2>Posts</h2>
		<p> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. </p>
	</div> -->
	</main>
	<footer>
		<span><img src="assets/copyleft.svg" width="12" height="12"/> 2020-2022 j-james </span>
	</footer>
</body>
</html>
<!--

*/

/*
 * HTML ::= '<!DOCTYPE html>' (NODE)*
 * NODE ::= '<'TAG (' ' WORD '=' ('"'TEXT'"' | TEXT))* '>' (NODE)* '</' TAG '>'
 *         | '<'SINGLE_TAG (' ' WORD '=' ('"'TEXT'"' | TEXT))* ('>'|'/>')
 *         | (TEXT | NODE)*
 * TEXT ::= UNICODE - {'"'} + {'\"'}
 * TAG ::= 'body' | 'div' | ...
 * SINGLE_TAG ::= 'img' | ...
 * (note that \forall T \in SINGLE_TAG, T \notin TAG)
 */
public class HtmlParser {

    private enum ParserState {
        HTML,
        OPENING_TAG, KEY, VALUE,
        SINGLE_QUOTE, DOUBLE_QUOTE,
        UNKNOWN_TAG, CLOSING_TAG,
    }

    public static ArrayList<Node> parseHtmlLL(String input) {

        var result = new ArrayList<Node>();
        var unfinished = new ArrayDeque<ElementNode>();
        var currentTag = "";
        var currentAttributes = new ArrayList<Pair<String, String>>();
        var currentKey = "";
        var currentValue = "";
        var currentText = "";
        var previousChar = '\0';

        // We safely? assume to start outside of all nodes.
        ParserState state = ParserState.HTML;

        for (char c : input.toCharArray()) {
            // System.out.print(state);
            // System.out.println(" " + c + " " + currentText);
            switch (state) {
                case HTML:
                    switch (c) {
                        case '<':
                            state = ParserState.UNKNOWN_TAG;
                            if (!currentText.equals("")) {
                                if (unfinished.size() != 0) {
                                    unfinished.getLast().addChild(new TextNode(currentText));
                                } else {
                                    result.add(new TextNode(currentText));
                                }
                                currentText = "";
                            }
                            break; // FOOTGUN LANGUAGE DESIGN
                        default:
                            currentText += c;
                            break;
                    }
                    break;
                case UNKNOWN_TAG:
                    switch (c) {
                        case '/':
                            state = ParserState.CLOSING_TAG;
                            break;
                        case '>': // Why would you put <> in your HTML??? go away
                            state = ParserState.HTML;
                            currentText += "<>";
                            System.out.println("Why would you put <> in your HTML??? go away");
                            break;
                        // Currently doesn't handle <!DOCTYPE> different from any other tag
                        case '!': default:
                            state = ParserState.OPENING_TAG;
                            currentTag += c;
                            break;
                    }
                    break; // FOOTGUN LANGUAGE DESIGN STRIKES AGAIN
                case OPENING_TAG:
                    switch (c) {
                        case '>':
                            state = ParserState.HTML;
                            var node = new ElementNode(currentTag, currentAttributes);
                            System.out.println("Adding ElementNode " + currentTag);
                            System.out.println("Current size of unfinished: " + unfinished.size());
                            if (unfinished.size() != 0) {
                                unfinished.getLast().addChild(node);
                                unfinished.add(node);
                            } else {
                                result.add(node);
                                unfinished.add((ElementNode) result.get(result.size() - 1));
                            }
                            currentTag = "";
                            currentAttributes = new ArrayList<>();
                            break;
                        case ' ': case '\n':
                            state = ParserState.KEY;
                            break;
                        default:
                            currentTag += c;
                            break;
                    }
                    break;
                case CLOSING_TAG:
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
                    break;
                case KEY:
                    switch (c) {
                        case '>':
                            state = ParserState.HTML;
                            var node = new ElementNode(currentTag, currentAttributes);
                            System.out.println("Adding ElementNode " + currentTag);
                            System.out.println("Current size of unfinished: " + unfinished.size());
                            if (unfinished.size() != 0) {
                                unfinished.getLast().addChild(node);
                                unfinished.add(node);
                            } else {
                                result.add(node);
                                unfinished.add((ElementNode) result.get(result.size() - 1));
                            }
                            currentTag = "";
                            currentAttributes = new ArrayList<>();
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
                    break;
                case VALUE:
                    switch (c) {
                        case '\'':
                            state = ParserState.SINGLE_QUOTE;
                            break;
                        case '\"':
                            state = ParserState.DOUBLE_QUOTE;
                            break;
                        case ' ': case '\n':
                            currentAttributes.add(new Pair<>(currentKey, currentValue));
                            currentKey = "";
                            currentValue = "";
                        case '>':
                            if (!currentKey.equals("") || !currentValue.equals("")) {
                                currentAttributes.add(new Pair<>(currentKey, currentValue));
                                currentKey = "";
                                currentValue = "";
                            }
                            state = ParserState.HTML;
                            var node = new ElementNode(currentTag, currentAttributes);
                            System.out.println("Adding ElementNode " + currentTag);
                            System.out.println("Current size of unfinished: " + unfinished.size());
                            if (unfinished.size() != 0) {
                                unfinished.getLast().addChild(node);
                                unfinished.add(node);
                            } else {
                                result.add(node);
                                unfinished.add((ElementNode) result.get(result.size() - 1));
                            }
                            currentTag = "";
                            currentAttributes = new ArrayList<>();
                            break;
                        default:
                            currentValue += c;
                            break;
                    }
                    break;
                case SINGLE_QUOTE:
                    switch (c) {
                        case '\'':
                            if (previousChar != '\\') {
                                state = ParserState.VALUE;
                                previousChar = '\0';
                            } else {
                                currentValue += c;
                                previousChar = c;
                            }
                            break;
                        default:
                            currentValue += c;
                            previousChar = c;
                            break;
                    }
                    break;
                case DOUBLE_QUOTE:
                    switch (c) {
                        case '\"':
                            if (previousChar != '\\') {
                                state = ParserState.VALUE;
                                previousChar = '\0';
                            } else {
                                currentValue += c;
                                previousChar = c;
                            }
                        default:
                            currentValue += c;
                            previousChar = c;
                            break;
                    }
                    break;
            }
        }
        return result;
    }

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
}
