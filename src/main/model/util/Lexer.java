package model.util;

import java.util.*;

// General-purpose Lexer
public class Lexer {

    // private static final Set<String> whitespace = new HashSet<String>(" ", "\n");

    // unused, helper function for if we implement finding identifers longer than a character
    private static int longestDelimiter(Set<String> delimiters) {
        int longestDelimiter = 0;
        for (String delimiter : delimiters) {
            if (delimiter.length() > longestDelimiter) {
                longestDelimiter = delimiter.length();
            }
        }
        return longestDelimiter;
    }

    /**
     * Lexes a "free-form" language. "free-form" has a specific meaning here that's important to preserve:
     * "free-form" means that _additional_ whitespace characters do not affect the language: e.g. two newlines
     * instead of one, four spaces instead of two, etc. They are _not_ "whitespace-insensitive", which is usually
     * a misnomer.
     * The name's a bit of a joke: free-form languages are generally referred to as whitespace-insensitive -->
     * insensitive == rude. Jokes are funnier when you have to explain them.
     * Also, insensitiveLex() and freeformLex() aren't really that good of names.
     *
     * NOTE: This lexer only works with single-character deliminators.
     * TODO: deduplicate whitespace
     */
    // public static ArrayList<String> rudeLex(String input, Set<Character> delimiters) {}

    /**
     * We might as well implement a lexer for non-free-form languages, but whatever. We won't use it.
     */
    public static ArrayList<String> sensitiveLex(String input, Set<Character> delimiters) {
        // int longestDelimiter = longestDelimiter(delimiters);

        ArrayList<String> tokens = new ArrayList<String>();
        String currentToken = "";
        // terrible c-style for loop because we may need to manipulate the index in the future
        for (int i = 0; i < input.length(); i++) {
            char nextToken = input.charAt(i);
            if (delimiters.contains(nextToken)) {
                if (!currentToken.equals("")) {
                    tokens.add(currentToken);
                }
                tokens.add(Character.toString(nextToken));
                currentToken = "";
            } else {
                currentToken += input.charAt(i);
            }
        }
        return tokens;
    }
}
