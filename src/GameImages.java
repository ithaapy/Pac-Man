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
    public BufferedImage yellowGhostSheet;
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
            wallImage = readRequiredImage(resourceClass, "assets/tile/wall.png");

            // Blue ghost sheet file tidak ada di assets/ghost folder kamu.
            // Jadi pakai sheet yellowG.png agar game tetap jalan tanpa ubah asset.
            yellowGhostSheet = readRequiredImage(resourceClass, "assets/ghost/yellowG_ver2.png");

            orangeGhostSheet = readRequiredImage(resourceClass, "assets/ghost/orangeG_ver2.png");
            pinkGhostSheet = readRequiredImage(resourceClass, "assets/ghost/pinkG_ver2.png");
            redGhostSheet = readRequiredImage(resourceClass, "assets/ghost/redG_ver2.png");



            // Nama file pacman juga berbeda (punya versi pacmanUp/pacmanDown, dll).
            pacmanUpSheet = readRequiredImage(resourceClass, "assets/player/PacMan_up.png");
            pacmanDownSheet = readRequiredImage(resourceClass, "assets/player/PacMan_down.png");
            pacmanLeftSheet = readRequiredImage(resourceClass, "assets/player/PacMan_left.png");
            pacmanRightSheet = readRequiredImage(resourceClass, "assets/player/PacMan_right.png");
        } catch (IOException e) {
            throw new RuntimeException("Gagal load gambar asset: " + e.getMessage(), e);
        }
    }

    private static BufferedImage readRequiredImage(Class<?> resourceClass, String path) throws IOException {
        java.net.URL url = resourceClass.getResource(path);
        if (url == null) {
            throw new IllegalArgumentException(
                    "Asset not found on classpath: " + path + " (resourceClass=" + resourceClass.getName() + ")");
        }
        return ImageIO.read(url);
    }

    private static BufferedImage readFirstExisting(Class<?> resourceClass, String[] paths) throws IOException {
        IllegalArgumentException lastError = null;
        for (String path : paths) {
            try {
                return readRequiredImage(resourceClass, path);
            } catch (IllegalArgumentException ex) {
                lastError = ex;
            }
        }
        // If none exist, throw the last meaningful exception
        if (lastError != null) throw lastError;
        throw new IllegalArgumentException("No asset path provided");
    }
}

