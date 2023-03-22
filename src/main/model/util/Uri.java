package model.util;

import java.util.Set;

public record Uri(String scheme, String authority, String userinfo, String host, int port, String path, String query, String fragment) {
    private static final Set<Character> GenDelims = Set.of(':', '/', '?', '#', '[', ']', '@');
    private static final Set<Character> SubDelims = Set.of('!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=');

    public Uri parse(String input) {
        // todo
        return null;
    }

    // percent-encode an arbitrary string
    public String encode(String input) {
        String buffer = "";

        for (char c : input.toCharArray()) {
            if (GenDelims.contains(c) || SubDelims.contains(c)) {
                // todo
            } else {
                buffer += c;
            }
        }
        return buffer;
    }
}

/*
  scheme*: string    # :
  authority*: string # //
  userinfo*: string  # @
  host*: string      # .
  port*: int         # :
  path*: string      # /
  query*: string     # ?
  fragment*: string  # #
*/