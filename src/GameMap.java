import java.util.HashSet;

/**
 * GameMap = nyimpen layout papan permainan (TILE_MAP) dan tugas "membaca"
 * layout itu jadi objek-objek Wall, Food, Ghost, dan Player yang sebenarnya.
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
                    ghosts.add(new Ghost(images.blueGhostImage, x, y, tileSize, tileSize));
                } else if (tile == 'o') {
                    ghosts.add(new Ghost(images.orangeGhostImage, x, y, tileSize, tileSize));
                } else if (tile == 'p') {
                    ghosts.add(new Ghost(images.pinkGhostImage, x, y, tileSize, tileSize));
                } else if (tile == 'r') {
                    ghosts.add(new Ghost(images.redGhostImage, x, y, tileSize, tileSize));
                } else if (tile == 'P') {
                    player = new Player(images.pacmanUpImage, images.pacmanDownImage,
                            images.pacmanLeftImage, images.pacmanRightImage,
                            x, y, tileSize, tileSize);
                } else if (tile == ' ') {
                    foods.add(new Food(x + 14, y + 14, 4, 4));
                }
            }
        }
    }
}