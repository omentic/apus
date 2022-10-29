package model.util;

import org.json.JSONObject;

/**
 * This Node represents an abstract relationship between ElementNode and TextNode.
 * It's extremely helpful / necessary for Lists of arbitrary ElementNodes/TextNodes.
 */
public interface Node {
    // Return a representation of the Node. Useful for debugging.
    public String getData();

    public JSONObject serialize();
}
