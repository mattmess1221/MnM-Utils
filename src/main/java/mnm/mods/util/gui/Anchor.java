package mnm.mods.util.gui;

public enum Anchor {

    TOP_LEFT(true, true),
    TOP_RIGHT(true, false),
    BOTTOM_LEFT(false, true),
    BOTTOM_RIGHT(false, false);

    private boolean top;
    private boolean left;

    private Anchor(boolean top, boolean left) {
        this.top = top;
        this.left = left;
    }

    public boolean isAnchoredLeft() {
        return this.left;
    }

    public boolean isAnchoredTop() {
        return this.top;
    }
}
