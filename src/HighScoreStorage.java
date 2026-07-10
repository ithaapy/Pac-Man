import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * HighScoreStorage = baca & simpan high score ke file teks (highscore.txt)
 * di folder yang sama tempat game dijalankan, biar nilainya nyimpen
 * walaupun game-nya ditutup terus dibuka lagi.
 */
public class HighScoreStorage {
    private static final String FILE_NAME = "highscore.txt";

    // Baca high score dari file. Kalau file belum ada / rusak, balikin 0.
    public static int load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return 0;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null) {
                return 0;
            }
            return Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    // Simpan high score baru ke file (nimpa isi lama)
    public static void save(int highScore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            // Gagal nyimpen bukan hal fatal, game tetap lanjut jalan
            System.err.println("Gagal simpan high score: " + e.getMessage());
        }
    }
}