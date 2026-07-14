import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * GameImages = tempat load semua sprite sheet & gambar statis sekali di awal.
 * Ghost & Player sekarang berupa SPRITE SHEET (banyak frame animasi),
 * bukan lagi 1 gambar statis. Pemotongan jadi frame dilakukan di GameMap.
 */
public class GameImages {
    public BufferedImage logoImage;
    public BufferedImage panelBackground;   // Bg3.png
    public BufferedImage buttonSheet;       // button.png

   
    // [0]=Start idle,  [1]=Start klik
    // [2]=Home idle,   [3]=Home klik
    // [4]=Restart idle,[5]=Restart klik
    // [6]=Exit idle,   [7]=Exit klik
    public BufferedImage[] buttonStartFrames;
    public BufferedImage[] buttonHomeFrames;
    public BufferedImage[] buttonRestartFrames;
    public BufferedImage[] buttonExitFrames;

    public BufferedImage pacIconImage;

    public BufferedImage wallImage;

    // Titik makanan kecil, sekarang pakai gambar koin, bukan fillRect() lagi
    public BufferedImage powerFoodImage;

    // Font pixel custom buat semua teks di game (score, judul, nyawa, dst)
    public Font pixelFont;

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
            wallImage = readRequiredImage(resourceClass, "/assets/tile/wall.png");
            powerFoodImage = readRequiredImage(resourceClass, "/assets/item/powerFood.png");
            pixelFont = loadPixelFont(resourceClass, "/assets/font/DePixelHalbfett.ttf");

            // Blue ghost sheet file tidak ada di assets/ghost folder kamu.
            // Jadi pakai sheet yellowG.png agar game tetap jalan tanpa ubah asset.
            yellowGhostSheet = readRequiredImage(resourceClass, "/assets/ghost/yellowG_ver2.png");

            orangeGhostSheet = readRequiredImage(resourceClass, "/assets/ghost/orangeG_ver2.png");
            pinkGhostSheet = readRequiredImage(resourceClass, "/assets/ghost/pinkG_ver2.png");
            redGhostSheet = readRequiredImage(resourceClass, "/assets/ghost/redG_ver2.png");



            // Nama file pacman juga berbeda (punya versi pacmanUp/pacmanDown, dll).
            pacmanUpSheet = readRequiredImage(resourceClass, "/assets/player/PacMan_up.png");
            pacmanDownSheet = readRequiredImage(resourceClass, "/assets/player/PacMan_down.png");
            pacmanLeftSheet = readRequiredImage(resourceClass, "/assets/player/PacMan_left.png");
            pacmanRightSheet = readRequiredImage(resourceClass, "/assets/player/PacMan_right.png");

            pacIconImage = readRequiredImage(resourceClass, "/assets/item/pacIcon.png");

            logoImage = readRequiredImage(resourceClass, "/assets/ui/logo.png");

            panelBackground = readRequiredImage(resourceClass, "/assets/ui/Bg3.png");
            buttonSheet = readRequiredImage(resourceClass, "/assets/ui/Button_ver2.png");

            BufferedImage[] buttons = SpriteSheet.cutGrid(buttonSheet, 32, 32, 2, 4);
            buttonStartFrames   = new BufferedImage[] { buttons[0], buttons[1] };
            buttonHomeFrames    = new BufferedImage[] { buttons[2], buttons[3] };
            buttonRestartFrames = new BufferedImage[] { buttons[4], buttons[5] };
            buttonExitFrames    = new BufferedImage[] { buttons[6], buttons[7] };
            

        } catch (IOException e) {
            
            throw new RuntimeException("Gagal load gambar/font asset: " + e.getMessage(), e);
        }
    }

        private static BufferedImage blurImage(BufferedImage src, int kernelSize) {
        if (kernelSize % 2 == 0) kernelSize++; // harus ganjil

        float weight = 1.0f / (kernelSize * kernelSize);
        float[] matrix = new float[kernelSize * kernelSize];
        java.util.Arrays.fill(matrix, weight);

        java.awt.image.Kernel kernel = new java.awt.image.Kernel(kernelSize, kernelSize, matrix);
        java.awt.image.ConvolveOp op = new java.awt.image.ConvolveOp(kernel, java.awt.image.ConvolveOp.EDGE_ZERO_FILL, null);

        BufferedImage compatible = new BufferedImage(
                src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        compatible.getGraphics().drawImage(src, 0, 0, null);
        return op.filter(compatible, null);
    }

    // Load file font custom (.ttf) dari classpath, lalu daftarkan ke sistem
    // biar bisa dipakai kayak font biasa lewat deriveFont(style, size).
    private static Font loadPixelFont(Class<?> resourceClass, String path) throws IOException {
        try (InputStream is = resourceClass.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException(
                        "Font not found on classpath: " + path + " (resourceClass=" + resourceClass.getName() + ")");
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (FontFormatException e) {
            throw new IOException("Format font tidak valid: " + path, e);
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