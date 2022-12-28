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
        this.location = new Point(10, 20);
        this.dimension = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        for (Layout child : this.children) {
            child.layout();
            this.dimension.height += child.dimension.height;
        }
    }
}
