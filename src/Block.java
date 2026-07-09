import java.awt.Image;
import java.util.HashSet;

/**
 * Block = class dasar (parent) buat semua objek yang ada di papan permainan.
 * Wall, Food, Ghost, dan Player semuanya "mewarisi" (extends) class ini.
 */
public class Block {
    int x;
    int y;
    int width;
    int height;
    Image image;

    int startX;
    int startY;
    char direction = 'U'; // U D L R
    int velocityX = 0;
    int velocityY = 0;

    public Block(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }

    // Ganti arah gerak, terapkan ke posisi, lalu cek: kalau nabrak tembok, batalkan lagi
    public void updateDirection(char direction, int tileSize, HashSet<Block> walls) {
        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity(tileSize);
        this.x += this.velocityX;
        this.y += this.velocityY;

        for (Block wall : walls) {
            if (collision(this, wall)) {
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                this.direction = prevDirection;
                updateVelocity(tileSize);
                break;
            }
        }
    }

    // Hitung kecepatan gerak (velocityX/Y) berdasarkan arah saat ini
    public void updateVelocity(int tileSize) {
        if (this.direction == 'U') {
            this.velocityX = 0;
            this.velocityY = -tileSize / 4;
        } else if (this.direction == 'D') {
            this.velocityX = 0;
            this.velocityY = tileSize / 4;
        } else if (this.direction == 'L') {
            this.velocityX = -tileSize / 4;
            this.velocityY = 0;
        } else if (this.direction == 'R') {
            this.velocityX = tileSize / 4;
            this.velocityY = 0;
        }
    }

    // Balikin posisi ke titik awal (dipakai pas pacman/hantu mati atau map di-reset)
    public void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }

    // Static: dipakai buat cek tabrakan antar 2 Block mana pun (dari class manapun)
    public static boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }
}