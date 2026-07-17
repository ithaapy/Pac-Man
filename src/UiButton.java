import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

public class UiButton {
    public enum Type { IMAGE, TEXT }

    private final String label;
    private final BufferedImage[] frames;  // null = teks
    private final Rectangle bounds;
    private final Runnable action;
    private final Type type;

    private boolean pressed = false;
    private Timer pressTimer;

    // Tombol gambar animasi
    public UiButton(BufferedImage[] frames, Rectangle bounds, Runnable action) {
        this.type = Type.IMAGE;
        this.label = null;
        this.frames = frames;
        this.bounds = bounds;
        this.action = action;
    }

    // Tombol teks (Home)
    public UiButton(String label, Rectangle bounds, Runnable action) {
        this.type = Type.TEXT;
        this.label = label;
        this.frames = null;
        this.bounds = bounds;
        this.action = action;
    }

    public boolean contains(int x, int y) {
        return bounds.contains(x, y);
    }

    public void click() {
        if (type == Type.IMAGE && frames != null && frames.length > 1) {
            pressed = true;
            if (pressTimer != null) pressTimer.stop();
            pressTimer = new Timer(200, e -> {
                pressed = false;
                action.run();
                ((Timer) e.getSource()).stop();
            });
            pressTimer.setRepeats(false);
            pressTimer.start();
        } else {
            action.run();
        }
    }

    public void draw(Graphics g, Font font) {
        if (type == Type.IMAGE && frames != null) {
            BufferedImage img = pressed ? frames[1] : frames[0];
            g.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height, null);
        } else if (type == Type.TEXT && label != null) {
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int textX = bounds.x + (bounds.width - fm.stringWidth(label)) / 2;
            int textY = bounds.y + (bounds.height + fm.getAscent()) / 2 - 2;
            g.drawString(label, textX, textY);
        }
    }
}