import java.awt.Image;
import java.util.HashSet;
import java.util.Random;

/**
 * Ghost = hantu. Beda dari Wall/Food, hantu punya "kelakuan sendiri":
 * gerak sendiri secara random dan bisa nabrak pacman.
 */
public class Ghost extends Block {
    private static final Random random = new Random();
    private static final char[] DIRECTIONS = {'U', 'D', 'L', 'R'};

    public Ghost(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
    }

    // Pilih 1 arah random, lalu langsung update posisi sesuai arah itu
    public void moveRandomDirection(int tileSize, HashSet<Block> walls) {
        char newDirection = DIRECTIONS[random.nextInt(4)];
        updateDirection(newDirection, tileSize, walls);
    }

    // Dipanggil tiap frame: jalan sesuai velocity saat ini.
    // Kalau nabrak tembok / keluar batas papan, balikin posisi & ganti arah random.
    public void move(int tileSize, int boardWidth, HashSet<Block> walls) {
        // Aturan khusus: di baris pintu kandang hantu, hantu wajib gerak ke atas dulu
        if (this.y == tileSize * 9 && this.direction != 'U' && this.direction != 'D') {
            updateDirection('U', tileSize, walls);
        }

        this.x += this.velocityX;
        this.y += this.velocityY;

        for (Block wall : walls) {
            if (collision(this, wall) || this.x <= 0 || this.x + this.width >= boardWidth) {
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                moveRandomDirection(tileSize, walls);
                break;
            }
        }
    }
}