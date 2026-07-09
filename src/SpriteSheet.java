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

    // Ambil beberapa baris SEKALIGUS dari sprite sheet, dibaca row-major:
    // baris 1 kolom 1, baris 1 kolom 2, ..., baris 2 kolom 1, baris 2 kolom 2, dst.
    // Dipakai buat sheet berbentuk persegi (misal ghost 64x64 = 2x2) yang
    // animasinya nyambung lintas baris, bukan cuma 1 baris doang kayak cutRow().
    public static BufferedImage[] cutGrid(BufferedImage sheet, int frameWidth, int frameHeight,
                                           int rowCount, int columnCount) {
        BufferedImage[] frames = new BufferedImage[rowCount * columnCount];
        int i = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                int x = col * frameWidth;
                int y = row * frameHeight;
                if (x + frameWidth > sheet.getWidth() || y + frameHeight > sheet.getHeight()) {
                    int safeW = Math.min(frameWidth, sheet.getWidth() - x);
                    int safeH = Math.min(frameHeight, sheet.getHeight() - y);
                    frames[i] = sheet.getSubimage(x, y, safeW, safeH);
                } else {
                    frames[i] = sheet.getSubimage(x, y, frameWidth, frameHeight);
                }
                i++;
            }
        }
        return frames;
    }
}