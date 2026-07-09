import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * GameImages = tempat load semua sprite sheet & gambar statis sekali di awal.
 * Ghost & Player sekarang berupa SPRITE SHEET (banyak frame animasi),
 * bukan lagi 1 gambar statis. Pemotongan jadi frame dilakukan di GameMap.
 */
public class GameImages {
    public BufferedImage wallImage;

    // Tiap file ghost: 128x32 (4 kotak 32x32 berjejer ke samping)
    public BufferedImage blueGhostSheet;
    public BufferedImage orangeGhostSheet;
    public BufferedImage pinkGhostSheet;
    public BufferedImage redGhostSheet;

    // Tiap file pacman: 128x96 (4 kolom x 3 baris @32x32).
    // Baris pertama (row 0) yang dipakai sekarang buat animasi jalan.
    public BufferedImage pacmanUpSheet;
    public BufferedImage pacmanDownSheet;
    public BufferedImage pacmanLeftSheet;
    public BufferedImage pacmanRightSheet;

    public GameImages(Class<?> resourceClass) {
        try {
            wallImage = ImageIO.read(resourceClass.getResource("assets/tile/wall.png"));

            blueGhostSheet = ImageIO.read(resourceClass.getResource("assets/ghost/blueGhost.png"));
            orangeGhostSheet = ImageIO.read(resourceClass.getResource("assets/ghost/orangeGhost.png"));
            pinkGhostSheet = ImageIO.read(resourceClass.getResource("assets/ghost/pinkGhost.png"));
            redGhostSheet = ImageIO.read(resourceClass.getResource("assets/ghost/redGhost.png"));

            // Sesuaikan nama file ini kalau nama file punya kamu beda ya!
            pacmanUpSheet = ImageIO.read(resourceClass.getResource("assets/player/PacMan_up.png"));
            pacmanDownSheet = ImageIO.read(resourceClass.getResource("assets/player/PacMan_down.png"));
            pacmanLeftSheet = ImageIO.read(resourceClass.getResource("assets/player/PacMan_left.png"));
            pacmanRightSheet = ImageIO.read(resourceClass.getResource("assets/player/PacMan_right.png"));
        } catch (IOException e) {
            throw new RuntimeException("Gagal load gambar asset: " + e.getMessage(), e);
        }
    }
}