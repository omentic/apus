package persistance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonUtils {

    /**
     * REQUIRES: A valid filepath path, a writeable JSONObject json
     * EFFECTS: writes a String to a file
     */
    public static void writeToFile(JSONArray json, String path) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(path);
            writer.print(json);
            writer.close();
        } catch (Exception e) {
            System.out.printf("Write to file failed with %s", e.toString());
        }
    }

    /**
     * REQUIRES: a path to a valid file containing JSONObject-serialized data
     * EFFECTS: reads a serialized String into a JSONObject
     */
    public static JSONArray readFromFile(String path) {
        String content;
        JSONArray deread;
        try {
            content = Files.readString(Paths.get(path));
            deread = new JSONArray(content);
        } catch (Exception e) {
            System.out.println("Read from file failed with %s");
            deread = new JSONArray();
        }
        return deread;
    }
}
