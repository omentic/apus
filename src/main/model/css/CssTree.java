package model.css;

import java.util.ArrayList;

/**
 * This isn't really a tree. Do I even need this class?
 */
public class CssTree {
    public class CssProperty {
        private String attribute;
        private String value;

        public CssProperty(String attribute, String value) {
            this.attribute = attribute;
            this.value = value;
        }

        public String getAttribute() {
            return attribute;
        }

        public String getValue() {
            return value;
        }
    }

    public class CssRule {
        private String selectors;
        private ArrayList<CssProperty> properties;

        public CssRule(String selectors, ArrayList<CssProperty> properties) {
            this.selectors = selectors;
            this.properties = properties;
        }

        public String getSelectors() {
            return this.selectors;
        }

        public ArrayList<CssProperty> getProperties() {
            return this.properties;
        }
    }

    private ArrayList<CssRule> rules;

    public CssTree(ArrayList<CssRule> rules) {
        this.rules = rules;
    }

    public ArrayList<CssRule> getRules() {
        return this.rules;
    }
}
