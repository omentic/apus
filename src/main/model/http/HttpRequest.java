package model.http;

import model.util.Uri;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record HttpRequest(String method, String target, String version, ArrayList<Header> headers, String body) {

    private static final String CRLF = "\r\n";
    private static final String DefaultVersion = "HTTP 0.9";
    private static final Header[] DefaultHeaders = {new Header("User-Agent", "apus")};

    public HttpRequest(String method, String target, String version, ArrayList<Header> headers) {
        this(method, target, version, headers, "");
    }

    // note: we disregard the port, that is taken care of by the connecting socket
    public HttpRequest(String method, Uri uri, String version, String body) {
        // ah, java
        this(method, uri.path(), DefaultVersion, new ArrayList<>(Stream.concat(Stream.of(new Header("", "")), Stream.of(DefaultHeaders)).collect(Collectors.toList())));
    }

    public HttpRequest(String method, Uri uri, String version) {
        this(method, uri, version, "");
    }

    public HttpRequest(String method, Uri uri) {
        this(method, uri, DefaultVersion);
    }

    // yeah, i probably should just use a hashmap
    public String host() throws MalformedRequestException {
        for (Header header : this.headers()) {
            if (header.key().equals("Host")) {
                return header.value();
            }
        }
        throw new MalformedRequestException();
    }

    public static class MalformedRequestException extends Exception {}

    public String raw() {
        String buffer = "";
        buffer += this.method() + " " + this.target() + " " + this.version() + CRLF;
        // hmm maybe i shouldn't use a hashmap
        for (Header header : this.headers()) {
            buffer += header.key() + " " + header.value() + CRLF;
        }
        if (!this.body().equals("")) {
            buffer += this.body() + CRLF;
        }
        buffer += CRLF;
        return buffer;
    }
}
