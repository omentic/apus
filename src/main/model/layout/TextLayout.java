package model.layout;

import model.html.TextNode;

public class TextLayout extends Layout {

    public TextLayout(TextNode node, Layout parent) {
        super(node, parent);
    }

    // recursively construct the layout tree
    public void layout() {
        this.location.x = this.previousSibling
            .map(sibling -> sibling.location.x + sibling.dimension.width)
            .orElseGet(() -> this.parent.location.x);
        this.location.y = this.parent.location.y;

        this.dimension.height = TEXT_HEIGHT_CONSTANT;
        this.dimension.width = this.associatedNode.data().length() * TEXT_WIDTH_CONSTANT;
    }
}
