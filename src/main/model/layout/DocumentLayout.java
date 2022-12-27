package model.layout;

import java.awt.*;

public class DocumentLayout extends Layout {

    /*
     * INCREDIBLY UNSAFE - but this is actually fine with our code design
     * We only reference the node / parent layout from the child layout,
     * and so as we aren't referencing them here it (should) be fine
     */
    public DocumentLayout() {
        super(null, null);
    }

    // recursively construct the layout tree
    public void layout() {
        this.setLocation(new Point(10, 20));
        this.setDimension(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        for (Layout child : this.getChildren()) {
            child.layout();
            this.setHeight(this.getHeight() + child.getHeight());
        }
    }
}
