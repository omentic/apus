package model.http;

import model.util.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// no static classes?? o_o
// add support for http 0.9 and also http 1.1 as 0.9
public class Http {
    private static final String CRLF = "\r\n";

    public static HttpResponse fetch(HttpRequest request, int port) throws IOException, HttpResponse.InvalidResponseException {
        try (Socket socket = new Socket(request.host(), port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            out.println(request.raw());
            String bufferOne = "";
            String bufferTwo = "";
            String response = "";
            while (!bufferOne.equals(CRLF) && !bufferTwo.equals(CRLF)) {
                bufferTwo = bufferOne;
                bufferOne = in.readLine() + CRLF;
                response += bufferOne;
            }
            return HttpResponse.parse(response);
        } catch (HttpRequest.MalformedRequestException e) {
            System.out.println("Failed to make request: " + e.getMessage());
            return new HttpResponse(600, "Malformed Request");
        }
    }

    public static HttpResponse get(Uri location) {
        // todo
        return null;
    }

    public static HttpResponse post(Uri location) {
        // todo
        return null;
    }
}
