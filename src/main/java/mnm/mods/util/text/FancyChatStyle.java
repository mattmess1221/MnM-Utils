package mnm.mods.util.text;

import mnm.mods.util.Color;

public class FancyChatStyle {

    private Color underline;
    private Color highlight;

    public Color getUnderline() {
        if (underline == null)
            underline = new Color(0);
        return underline;
    }

    public void setUnderline(Color underline) {
        this.underline = underline;
    }

    public Color getHighlight() {
        if (highlight == null)
            highlight = new Color(0);
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
        return String.format("FancyStyle{underline=%s, hightlight=%s}", underline, highlight);
    }

}
