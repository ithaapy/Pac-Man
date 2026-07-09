import java.awt.image.BufferedImage;

/**
 * SpriteSheet = helper buat motong 1 file gambar besar (sprite sheet)
 * jadi potongan-potongan kecil (frame) berukuran sama, siap dianimasikan.
 */
public class SpriteSheet {
    // Ambil satu baris (row) dari sprite sheet, sebanyak columnCount kotak,
    // masing-masing berukuran frameWidth x frameHeight.
    // row = 0 berarti baris paling atas.
    public static BufferedImage[] cutRow(BufferedImage sheet, int frameWidth, int frameHeight,
                                          int row, int columnCount) {
        BufferedImage[] frames = new BufferedImage[columnCount];
        for (int col = 0; col < columnCount; col++) {
            int x = col * frameWidth;
            int y = row * frameHeight;
            // Safety check: pastikan request subimage masih di dalam ukuran sheet.
            if (x + frameWidth > sheet.getWidth() || y + frameHeight > sheet.getHeight()) {
                // fallback: pakai area yang available (crop sampai batas)
                int safeW = Math.min(frameWidth, sheet.getWidth() - x);
                int safeH = Math.min(frameHeight, sheet.getHeight() - y);
                frames[col] = sheet.getSubimage(x, y, safeW, safeH);
            } else {
                frames[col] = sheet.getSubimage(x, y, frameWidth, frameHeight);
            }
        }
        return frames;
    }
}