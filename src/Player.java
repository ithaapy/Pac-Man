import java.awt.image.BufferedImage;
import java.util.HashSet;

/**
 * Player = pacman. Tiap arah (up/down/left/right) punya animasi jalan sendiri
 * (4 frame per arah, diambil dari baris pertama sprite sheet arah itu).
 * Ini contoh POLYMORPHISM: updateDirection() di-override supaya selain
 * gerak, animasi yang aktif juga ikut ganti sesuai arah baru.
 */
public class Player extends Block {
    private static final int TICKS_PER_FRAME = 4; // makin kecil = animasi makin cepat

    private SpriteAnimation upAnimation;
    private SpriteAnimation downAnimation;
    private SpriteAnimation leftAnimation;
    private SpriteAnimation rightAnimation;
    private SpriteAnimation currentAnimation;

    public Player(BufferedImage[] upFrames, BufferedImage[] downFrames,
                  BufferedImage[] leftFrames, BufferedImage[] rightFrames,
                  int x, int y, int width, int height) {
        super(rightFrames[0], x, y, width, height); // default gambar awal: menghadap kanan

        this.upAnimation = new SpriteAnimation(upFrames, TICKS_PER_FRAME);
        this.downAnimation = new SpriteAnimation(downFrames, TICKS_PER_FRAME);
        this.leftAnimation = new SpriteAnimation(leftFrames, TICKS_PER_FRAME);
        this.rightAnimation = new SpriteAnimation(rightFrames, TICKS_PER_FRAME);
        this.currentAnimation = rightAnimation;
    }

    // Override dari Block: perilaku dasar (gerak + cek nabrak tembok) tetap
    // dipakai lewat super.updateDirection(), lalu ditambah 1 hal baru:
    // ganti animasi yang lagi aktif sesuai arah barunya.
    @Override
    public void updateDirection(char direction, int tileSize, HashSet<Block> walls) {
        super.updateDirection(direction, tileSize, walls);
        switchAnimation();
    }

    private void switchAnimation() {
        if (this.direction == 'U') {
            currentAnimation = upAnimation;
        } else if (this.direction == 'D') {
            currentAnimation = downAnimation;
        } else if (this.direction == 'L') {
            currentAnimation = leftAnimation;
        } else if (this.direction == 'R') {
            currentAnimation = rightAnimation;
        }
    }

    // Dipanggil tiap frame game (dari GameBoard) buat jalanin animasi mulut kunyah
    public void animateTick() {
        currentAnimation.tick();
        this.image = currentAnimation.getCurrentFrame();
    }

    @Override
    public void reset() {
        super.reset();
        upAnimation.reset();
        downAnimation.reset();
        leftAnimation.reset();
        rightAnimation.reset();
    }
}