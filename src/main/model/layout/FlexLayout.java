package model.layout;

import model.html.Node;

// incredibly complicated magic
// https://drafts.csswg.org/css-flexbox/#layout-algorithm
public class FlexLayout extends Layout {

    public FlexLayout(Node node, Layout parent) {
        super(node, parent);
    }

    // todo: we'll cheese it, and treat it like a sideways block
    public void layout() {
    }
}
