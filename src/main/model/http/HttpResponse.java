package model.http;

import java.util.ArrayList;

public record HttpResponse(String version, int status, String reason, String body, ArrayList<Header> headers) {
    // for constructing local error responses
    public HttpResponse(int status, String reason) {
        this("HTTP 1.1", status, reason, "", new ArrayList<Header>());
    }

    // probably inefficient but eh
    public static HttpResponse parse(String response) throws InvalidResponseException {
        try {
            var split = response.split("\\r\\n\\r\\n");
            var lines = split[0].split("\\r\\n");
            var body = split[1];
            var start = lines[0].split(" ", 3);
            var version = start[0];
            var status = Integer.parseInt(start[1]);
            var reason = start[2];

            var headers = new ArrayList<Header>();
            for (int i = 1; i < lines.length; i++) {
                split = lines[i].split(": ", 2);
                headers.add(new Header(split[0], split[1]));
            }
            return new HttpResponse(version, status, reason, body, headers);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidResponseException();
        }
    }

    public static class InvalidResponseException extends Exception {}
}
