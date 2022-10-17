package model.css;

import java.util.ArrayList;

/**
 * This lexer splits an input by whitespace, brackets, and semicolons.
 * Brackets and semicolons are included in the lexed output, whitespace is not.
 * <br>
 * CSS, thankfully, is far more rigid and less-forgiving of errors than HTMl.
 * It also has multiple layers of fallback for errors: ranging from: "ignore this
 * property", to "ignore this rule", to "this isn't fucking CSS" and ignore it all.
 * <br>
 * Still, even though we don't have to deal with garbage like escaped quotes (future edit: whoops, yes we do) and
 * what not, we'll still implement our lexer with a for loop instead of split() for future optimizations.
 */
public class CssLexer {

    public static ArrayList<String> lex(String input) {
        String token = "";
        ArrayList<String> tokens = new ArrayList<>();
        boolean inSingleQuotes = false;
        boolean inDoubleQuotes = false;
        char previous = '\0';

        for (char i : input.toCharArray()) {
            // i HATE fallthrough switch statements
            switch (i) {
                case '{': case '}': case ';': case ':':
                case ' ': case '\n': case '\t':
                    if (!inSingleQuotes && !inDoubleQuotes) {
                        if (!token.equals("")) {
                            tokens.add(token);
                            token = "";
                        }
                        switch (i) {
                            case '{': case '}': case ';': case ':':
                                tokens.add(Character.toString(i));
                                break;
                            case ' ': case '\n': case '\t':
                                break;
                        }
                    } else {
                        token += i;
                    }
                    break;
                // intentional use of footgun behavior
                case '"':
                    if (previous != '\\') {
                        inDoubleQuotes = !inDoubleQuotes;
                    }
                case '\'':
                    if (previous != '\\') {
                        inSingleQuotes = !inSingleQuotes;
                    }
                default:
                    token += i;
                    break;
            }
            previous = i;
        }
        return tokens;
    }
}
