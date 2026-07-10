import java.awt.image.BufferedImage;

/**
 * SpriteAnimation = pemutar animasi. Nyimpen kumpulan frame (dari SpriteSheet),
 * lalu ganti frame yang aktif tiap sekian "tick" (tiap kali game loop jalan).
 */
public class SpriteAnimation {
    private BufferedImage[] frames;
    private int currentFrame = 0;
    private int tickCounter = 0;
    private int ticksPerFrame;

    public SpriteAnimation(BufferedImage[] frames, int ticksPerFrame) {
        this.frames = frames;
        this.ticksPerFrame = ticksPerFrame;
    }

    // Panggil ini tiap kali game loop jalan (tiap frame GAME, bukan tiap frame ANIMASI).
    // Cuma pindah ke frame animasi berikutnya kalau sudah lewat ticksPerFrame kali.
    public void tick() {
        tickCounter++;
        if (tickCounter >= ticksPerFrame) {
            tickCounter = 0;
            currentFrame = (currentFrame + 1) % frames.length;
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    public void reset() {
        currentFrame = 0;
        tickCounter = 0;
    }
}