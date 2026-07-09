import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * GameImages = tempat nyimpen & load semua gambar (tembok, hantu, pacman)
 * sekali aja di awal. Tujuannya biar GameBoard gak penuh kode loading gambar.
 */
public class GameImages {
    public Image wallImage;
    public Image blueGhostImage;
    public Image orangeGhostImage;
    public Image pinkGhostImage;
    public Image redGhostImage;

    public Image pacmanUpImage;
    public Image pacmanDownImage;
    public Image pacmanLeftImage;
    public Image pacmanRightImage;

    // resourceClass dikirim dari GameBoard (biasanya getClass()) supaya
    // path assets tetap relatif ke lokasi file class itu, sama seperti kode aslinya
    public GameImages(Class<?> resourceClass) {
        wallImage = new ImageIcon(resourceClass.getResource("assets/tile/wall.png")).getImage();
        blueGhostImage = new ImageIcon(resourceClass.getResource("assets/ghost/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(resourceClass.getResource("assets/ghost/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(resourceClass.getResource("assets/ghost/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(resourceClass.getResource("assets/ghost/redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(resourceClass.getResource("assets/player/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(resourceClass.getResource("assets/player/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(resourceClass.getResource("assets/player/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(resourceClass.getResource("assets/player/pacmanRight.png")).getImage();
    }
}