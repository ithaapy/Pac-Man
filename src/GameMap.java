import java.awt.image.BufferedImage;
import java.util.HashSet;

/**
 * GameMap = nyimpen layout papan permainan (TILE_MAP) dan tugas "membaca"
 * layout itu jadi objek-objek Wall, Food, Ghost, dan Player yang sebenarnya.
 * Sprite sheet ghost/pacman dipotong jadi frame animasi di sini.
 */
public class GameMap {
    // X = tembok, O = celah kosong di sisi kiri/kanan, P = pacman, spasi = makanan
    // Hantu: b = biru, o = oranye, p = pink, r = merah
    public static final String[] TILE_MAP = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    public static final int ROW_COUNT = TILE_MAP.length;
    public static final int COLUMN_COUNT = TILE_MAP[0].length();

    // Sprite sheet ghost sekarang 64x64: 2 baris x 2 kolom (32x32 tiap kotak)
    private static final int GHOST_FRAME_ROWS = 2;
    private static final int GHOST_FRAME_COLUMNS = 2;
    // Sprite sheet pacman punya 4 kolom di tiap barisnya (128x96),
    // tapi buat sekarang baru baris pertama (row 0) yang dipakai
    private static final int PLAYER_FRAME_COLUMNS = 4;
    private static final int ANIMATION_ROW = 0;

    // Ukuran gambar koin (titik makanan), dibuat sedikit lebih besar dari
    // fillRect lama (4x4) biar gambarnya kelihatan jelas, tetap dicenterin di tile
    private static final int FOOD_SIZE = 4;

    public HashSet<Block> walls = new HashSet<>();
    public HashSet<Food> foods = new HashSet<>();
    public HashSet<Ghost> ghosts = new HashSet<>();
    public Player player;

    private GameImages images;
    private int tileSize;

    public GameMap(GameImages images, int tileSize) {
        this.images = images;
        this.tileSize = tileSize;
    }

    // Baca ulang TILE_MAP baris per baris, kolom per kolom, lalu bikin objek
    // sesuai karakternya. Dipanggil pas game mulai dan tiap kali makanan habis.
    public void load() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < ROW_COUNT; r++) {
            for (int c = 0; c < COLUMN_COUNT; c++) {
                char tile = TILE_MAP[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                if (tile == 'X') {
                    walls.add(new Wall(images.wallImage, x, y, tileSize, tileSize));
                } else if (tile == 'b') {
                    ghosts.add(new Ghost(cutGhostFrames(images.yellowGhostSheet), x, y, tileSize, tileSize));
                } else if (tile == 'o') {
                    ghosts.add(new Ghost(cutGhostFrames(images.orangeGhostSheet), x, y, tileSize, tileSize));
                } else if (tile == 'p') {
                    ghosts.add(new Ghost(cutGhostFrames(images.pinkGhostSheet), x, y, tileSize, tileSize));
                } else if (tile == 'r') {
                    ghosts.add(new Ghost(cutGhostFrames(images.redGhostSheet), x, y, tileSize, tileSize));
                } else if (tile == 'P') {
                    BufferedImage[] upFrames = cutPlayerFrames(images.pacmanUpSheet);
                    BufferedImage[] downFrames = cutPlayerFrames(images.pacmanDownSheet);
                    BufferedImage[] leftFrames = cutPlayerFrames(images.pacmanLeftSheet);
                    BufferedImage[] rightFrames = cutPlayerFrames(images.pacmanRightSheet);
                    player = new Player(upFrames, downFrames, leftFrames, rightFrames,
                            x, y, tileSize, tileSize);
                } else if (tile == ' ') {
                    int offset = (tileSize - FOOD_SIZE) / 2;
                    foods.add(new Food(images.powerFoodImage, x + offset, y + offset, FOOD_SIZE, FOOD_SIZE));
                }
            }
        }
    }

    private BufferedImage[] cutGhostFrames(BufferedImage sheet) {
        return SpriteSheet.cutGrid(sheet, tileSize, tileSize, GHOST_FRAME_ROWS, GHOST_FRAME_COLUMNS);
    }

    private BufferedImage[] cutPlayerFrames(BufferedImage sheet) {
        return SpriteSheet.cutRow(sheet, tileSize, tileSize, ANIMATION_ROW, PLAYER_FRAME_COLUMNS);
    }
}