package mnm.mods.util.text;

import mnm.mods.util.Color;

public class FancyChatStyle {

    private Color color;
    private Color underline;
    private Color highlight;

    public Color getColor() {
        if (underline == null)
            return Color.WHITE;
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getUnderline() {
        if (underline == null)
            return Color.of(0);
        return underline;
    }

    public void setUnderline(Color underline) {
        this.underline = underline;
    }

    public Color getHighlight() {
        if (highlight == null)
            return Color.of(0);
        return highlight;
    }

    public void setHighlight(Color highlight) {
        this.highlight = highlight;
    }

    public FancyChatStyle createCopy() {
        FancyChatStyle fcs = new FancyChatStyle();
        fcs.highlight = highlight;
        fcs.underline = underline;
        return fcs;
    }

    @Override
    public String toString() {
        return String.format("FancyStyle{color=%s, underline=%s, hightlight=%s}", color, underline, highlight);
    }

}
