import java.awt.Image;

/**
 * Wall = tembok. Sifatnya cuma diam di tempat sebagai penghalang,
 * jadi gak perlu tambahan behavior apa-apa dari Block.
 */
public class Wall extends Block {
    public Wall(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
    }
}