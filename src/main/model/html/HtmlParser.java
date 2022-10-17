package model.html;

import java.util.*;

import model.html.HtmlTree;
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

    public static ArrayList<HtmlTree> parseHtmlLL(String input) {

        var result = new ArrayList<HtmlTree>();
        var unfinished = new ArrayDeque<HtmlTree>();
        var currentTag = "";
        var currentAttributes = new ArrayList<Pair<String, String>>();
        var currentKey = "";
        var currentValue = "";
        var currentText = "";

        // We safely? assume to start outside of all nodes.
        ParserState state = ParserState.HTML;

        for (char c : input.toCharArray()) {
            switch (state) {
                case HTML:
                    switch (c) {
                        case '<':
                            if (!currentText.equals("")) {
                                // unfinished.add(text) idk
                            }

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
                        case '>':
                            state = ParserState.HTML;
                            System.out.println("Why would you put <> in your HTML??? go away");
                            break;
                        default:
                            state = ParserState.OPENING_TAG;
                            currentTag += c;
                            break;
                    }
                case OPENING_TAG:
                    switch (c) {
                        case '>':
                            state = ParserState.HTML;
                            // unfinished.add(new HtmlTree(tag)
                            currentTag = "";
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
                            // IMPORTANT: we don't validate that closing tags correspond to an open tag
                            if (!isSelfClosingTag(currentTag)) {
                                //unknown.pop
                            }
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
                            if (currentAttributes.size() != 0) {
                                // unfinished.something idk new HtmlTree(tag=currentTag, attributes=currentAttributes)
                                currentAttributes.clear();
                            } else {
                                // unfinished.add(new HtmlTree(tag)
                            }
                            currentTag = "";
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
                            // unfinished.something idk new HtmlTree(tag=currentTag, attributes=currentAttributes)
                            currentAttributes.clear();
                        default:
                            currentValue += c;
                            break;
                    }
                    break;
                case SINGLE_QUOTE:
                    switch (c) {
                        case '\'':
                            state = ParserState.VALUE;
                        default:
                            currentValue += c;
                            break;
                    }
                    break;
                case DOUBLE_QUOTE:
                    switch (c) {
                        default:
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

    /*
    public static void parseHtmlLL(String input) {
        String tag = "";
        ArrayList<Pair<String, String>> attributes = new ArrayList<>();
        boolean inTag = false;
        boolean inAttribute = false; // for checking if we're in quotes

        for (int i = 0; i < input.length(); i++) {
            if (inTag) {
                if (inAttribute) {
                    switch (input.charAt(i)) {
                        case '\"'
                    }
                } else {
                    switch (input.charAt(i)) {

                    }
                }


            } else {
                switch (input.charAt(i)) {
                    case '<':
                }
            }
        }
    }

    private static void parseAttribute(String input) {

    }
*/

/*
    public static void parseHTML(ArrayList<String> input) {
        String data = "";
        ArrayList<ParseTree> children = new ArrayList<ParseTree>();

        boolean inTag = false;
        boolean tagComplete = false;

        for (String i : input) {
            if (inTag) {
                if (i.equals(">")) {
                    inTag = false;
                    tagComplete = true;
                    // remove ending tags and recursively parse out children
                } else {
                    data += i;
                }
            } else {
                if (i.equals("<")) {
                    inTag = true;
                }
            }

        }

    }*/
}
