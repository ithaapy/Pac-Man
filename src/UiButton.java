import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class UiButton {
    public enum Type { IMAGE, TEXT }

    private final String label;
    private final BufferedImage image;
    private final Rectangle bounds;
    private final Runnable action;
    private final Type type;

    public UiButton(String label, Rectangle bounds, Runnable action) {
        this.type = Type.TEXT;
        this.label = label;
        this.image = null;
        this.bounds = bounds;
        this.action = action;
    }

    public UiButton(BufferedImage image, Rectangle bounds, Runnable action) {
        this.type = Type.IMAGE;
        this.label = null;
        this.image = image;
        this.bounds = bounds;
        this.action = action;
    }

    public boolean contains(int x, int y) {
        return bounds.contains(x, y);
    }

    public void click() {
        action.run();
    }

    public void draw(Graphics g, Font font) {
        if (type == Type.IMAGE && image != null) {
            g.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
        } else if (type == Type.TEXT && label != null) {
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int textX = bounds.x + (bounds.width - fm.stringWidth(label)) / 2;
            int textY = bounds.y + (bounds.height + fm.getAscent()) / 2 - 2;
            g.drawString(label, textX, textY);
        }
    }
}