import java.awt.Image;

/**
 * Food = titik makanan kecil, digambar pakai gambar koin (smallCoin.png)
 * yang dioper dari GameMap, bukan lagi fillRect() manual.
 */
public class Food extends Block {
    public Food(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
    }
}