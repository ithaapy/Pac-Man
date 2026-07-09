import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Random;

/**
 * Ghost = hantu. Punya animasi jalan (looping 4 frame dari sprite sheet),
 * tapi gambarnya gak berubah sesuai arah (cuma 1 sisi/tampilan default).
 */
public class Ghost extends Block {
    private static final Random random = new Random();
    private static final char[] DIRECTIONS = {'U', 'D', 'L', 'R'};
    private static final int TICKS_PER_FRAME = 8; // makin kecil = animasi makin cepat

    private SpriteAnimation animation;

    public Ghost(BufferedImage[] frames, int x, int y, int width, int height) {
        super(frames[0], x, y, width, height);
        this.animation = new SpriteAnimation(frames, TICKS_PER_FRAME);
    }

    // Pilih 1 arah random, lalu langsung update posisi sesuai arah itu
    public void moveRandomDirection(int tileSize, HashSet<Block> walls) {
        char newDirection = DIRECTIONS[random.nextInt(4)];
        updateDirection(newDirection, tileSize, walls);
    }

    // Dipanggil tiap frame game: jalanin animasi + gerak sesuai velocity.
    // Kalau nabrak tembok / keluar batas papan, balikin posisi & ganti arah random.
    public void move(int tileSize, int boardWidth, HashSet<Block> walls) {
        animation.tick();
        this.image = animation.getCurrentFrame();

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

    @Override
    public void reset() {
        super.reset();
        animation.reset();
    }
}