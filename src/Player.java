import java.awt.Image;
import java.util.HashSet;

/**
 * Player = pacman-nya sendiri, dikontrol pemain lewat keyboard.
 * Ini contoh POLYMORPHISM: method updateDirection() di-override,
 * jadi selain gerak, gambar pacman juga ikut ganti sesuai arah.
 */
public class Player extends Block {
    private Image upImage;
    private Image downImage;
    private Image leftImage;
    private Image rightImage;

    public Player(Image upImage, Image downImage, Image leftImage, Image rightImage,
                  int x, int y, int width, int height) {
        super(rightImage, x, y, width, height); // default gambar awal: menghadap kanan
        this.upImage = upImage;
        this.downImage = downImage;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
    }

    // Override method dari Block: perilaku dasarnya (gerak + cek nabrak) tetap dipakai
    // lewat super.updateDirection(), tapi ditambah 1 hal baru: ganti gambar
    @Override
    public void updateDirection(char direction, int tileSize, HashSet<Block> walls) {
        super.updateDirection(direction, tileSize, walls);
        updateImage();
    }

    private void updateImage() {
        if (this.direction == 'U') {
            this.image = upImage;
        } else if (this.direction == 'D') {
            this.image = downImage;
        } else if (this.direction == 'L') {
            this.image = leftImage;
        } else if (this.direction == 'R') {
            this.image = rightImage;
        }
    }
}