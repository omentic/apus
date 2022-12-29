package model.layout;

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
        this.location.x = DEFAULT_X;
        this.location.y = DEFAULT_Y;

        for (Layout child : this.children) {
            child.layout();
            this.dimension.height = Math.max(this.dimension.height, (child.location.y + child.dimension.height) - this.location.y);
            this.dimension.width = Math.max(this.dimension.width, (child.location.x + child.dimension.width) - this.location.x);
        }
    }
}
