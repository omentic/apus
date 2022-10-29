package persistance;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilsTest {
    private static final String testPath = "data/test.cache";

    @Test
    void testWritingEmptyFile() {
        ArrayDeque<String> test = new ArrayDeque<>();
        JsonUtils.writeToFile(new JSONArray(test), testPath);

        JSONArray dugUp = JsonUtils.readFromFile(testPath);
        assertTrue(dugUp.isEmpty());
    }

    @Test
    void testWritingLists() {
        ArrayDeque<String> test = new ArrayDeque<>();
        test.add("/home/apropos/foo/bar"); // absolute path
        test.add("data"); // relative path
        test.add("/home/apropos/baz/bar"); // another path
        JsonUtils.writeToFile(new JSONArray(test), testPath);

        JSONArray dugUp = JsonUtils.readFromFile(testPath);
        for (int i = 0; i < dugUp.length(); i++) {
            assertTrue(test.removeFirst().equals(dugUp.get(i)));
        }
    }
}
