package model.html;

import java.util.ArrayList;

/**
 * We'll tokenize HTML by tags: disregarding the contents of the tag and attributes within the tag.
 * The file is also considered to be free-form here: whitespace duplicates are disregarded.
 */
public class HtmlLexer {

    // Takes a String of raw HTML, and tokenizes it for our parser.
    public static ArrayList<String> lex(String input) {
        String token = "";
        ArrayList<String> tokens = new ArrayList<>();
        boolean inTag = false;
        boolean inSingleQuotes = false;
        boolean inDoubleQuotes = false;

        for (char i : input.toCharArray()) {
            token += i;
            switch (i) {
                case '<':
                    if (!inSingleQuotes && !inDoubleQuotes) {
                        inTag = true;
                        if (!token.equals("<")) {
                            tokens.add(token.substring(0, token.length() - 1));
                            token = "<";
                        }
                    } else if (inTag) {
                        System.out.printf("Probably failing parser");
                    }
                    break;
                case '>':
                    if (!inSingleQuotes && !inDoubleQuotes) {
                        if (!inTag) {
                            System.out.printf("Probably failing parser");
                        }
                        inTag = false;
                        tokens.add(token);
                        token = "";
                    }
                    break;
                case '"':
                    if (!inSingleQuotes) {
                        inDoubleQuotes = !inDoubleQuotes;
                    }
                    break;
                case '\'':
                    if (!inDoubleQuotes) {
                        inSingleQuotes = !inSingleQuotes;
                    }
                    break;
            }
        }
        /**
         * When lexing invalid HTML: we may end up with trailing garbage: either an unfinished tag or extra text
         * (those are the only two options since this is just the lex step)
         */
        if (!token.equals("")) {
            if (inTag) {
                tokens.add(token + ">");
            } else {
                tokens.add(token);
            }
        }
        return tokens;
    }
}
