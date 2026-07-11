import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GameBoard = "otak" dari game ini. Isinya cuma logic jalannya permainan:
 * game loop, gambar ke layar, cek tabrakan, dan baca input keyboard.
 * Data map & pembuatan objek sudah dipindah ke GameMap.
 *
 * Layout layar sekarang ada 3 lapis:
 * 1. Border ungu di paling luar
 * 2. Header (Your Score / High Score) di bagian atas area gelap
 * 3. Area maze (game sebenarnya) di bawah header
 */
public class GameBoard extends JPanel implements ActionListener, KeyListener {
    private static final Color BORDER_COLOR = new Color(70, 58, 115);
    private static final Color BACKGROUND_COLOR = new Color(42, 40, 58);
    private static final int BORDER = 16;
    private static final int HEADER_HEIGHT = 70;
            
    private static final int FOOTER_HEIGHT = 50;   // area kosong bawah untuk nyawa
    private static final int LIFE_ICON_SIZE = 24;    // ukuran ikon pacman kecil
    private static final int LIFE_ICON_GAP = 8;      // jarak antar ikon

    private int tileSize = 32;
    private int boardWidth = GameMap.COLUMN_COUNT * tileSize;
    private int boardHeight = GameMap.ROW_COUNT * tileSize;

    // Ukuran total panel = area maze + border ungu + header skor
    private int totalWidth = boardWidth + BORDER * 2;
    private int totalHeight = boardHeight + HEADER_HEIGHT + FOOTER_HEIGHT + BORDER * 2;

    private GameImages images;
    private GameMap gameMap;

    private Timer gameLoop;
    private int score = 0;
    private int highScore;
    private int lives = 3;
    private boolean gameOver = false;

    public GameBoard() {
        setPreferredSize(new Dimension(totalWidth, totalHeight));
        setBackground(BORDER_COLOR);
        addKeyListener(this);
        setFocusable(true);

        highScore = HighScoreStorage.load();

        images = new GameImages(getClass());
        gameMap = new GameMap(images, tileSize);
        gameMap.load();

        // Kasih arah random awal buat semua hantu
        for (Ghost ghost : gameMap.ghosts) {
            ghost.moveRandomDirection(tileSize, gameMap.walls);
        }

        // 50ms per frame = 20fps
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Lapis 1: seluruh panel udah keisi warna ungu lewat setBackground()

        // Lapis 2: kotak gelap tempat header + maze berada
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(BORDER, BORDER, totalWidth - BORDER * 2, totalHeight - BORDER * 2);

        drawHeader(g);

        // Lapis 3: maze digambar lewat "sub-graphics" yang digeser & di-clip
        // ke area mazenya sendiri, jadi kode gambar tembok/food/dst gak perlu
        // tau-menau soal border atau header di luar sana.
        Graphics mazeGraphics = g.create(BORDER, BORDER + HEADER_HEIGHT, boardWidth, boardHeight);
        drawMaze(mazeGraphics);
        mazeGraphics.dispose();

        drawFooter(g);
    }

    private void drawFooter(Graphics g) {
    int footerTop = BORDER + HEADER_HEIGHT + boardHeight;
    int startX = BORDER + 10;  // sejajar padding "Your Score"
    int iconY = footerTop + (FOOTER_HEIGHT - LIFE_ICON_SIZE) / 2;

        for (int i = 0; i < lives; i++) {
            int iconX = startX + i * (LIFE_ICON_SIZE + LIFE_ICON_GAP);
            g.drawImage(
                images.pacIconImage,
                iconX,
                iconY,
                LIFE_ICON_SIZE,
                LIFE_ICON_SIZE,
                null
            );
        }
    
    }

    private void drawHeader(Graphics g) {
        Font labelFont = images.pixelFont.deriveFont(Font.PLAIN, 14f);
        Font valueFont = images.pixelFont.deriveFont(Font.PLAIN, 20f);
        Font titleFont = images.pixelFont.deriveFont(Font.PLAIN, 28f);

        g.setColor(Color.WHITE);

        // "Your Score" + nilainya, rata kiri
        g.setFont(labelFont);
        g.drawString("Your Score", BORDER + 10, BORDER + 24);
        g.setFont(valueFont);
        g.drawString(String.valueOf(score), BORDER + 10, BORDER + 50);

        // "High Score" + nilainya, rata kanan
        g.setFont(labelFont);
        FontMetrics labelMetrics = g.getFontMetrics();
        String highScoreLabel = "High Score";
        int labelWidth = labelMetrics.stringWidth(highScoreLabel);
        g.drawString(highScoreLabel, totalWidth - BORDER - 10 - labelWidth, BORDER + 24);

        g.setFont(valueFont);
        FontMetrics valueMetrics = g.getFontMetrics();
        String highScoreValue = String.valueOf(highScore);
        int valueWidth = valueMetrics.stringWidth(highScoreValue);
        g.drawString(highScoreValue, totalWidth - BORDER - 10 - valueWidth, BORDER + 50);

        // Judul game, ditaruh di tengah, di antara Your Score dan High Score
        g.setFont(titleFont);
        FontMetrics titleMetrics = g.getFontMetrics();
        String title = "Pac-Man";
        int titleWidth = titleMetrics.stringWidth(title);
        int titleX = (totalWidth - titleWidth) / 2;
        int titleY = BORDER + (HEADER_HEIGHT + titleMetrics.getAscent()) / 2 - 4;
        g.drawString(title, titleX, titleY);
    }

    // g di sini koordinatnya udah relatif ke pojok kiri-atas area maze (0,0)
    private void drawMaze(Graphics g) {
        Player pacman = gameMap.player;
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block wall : gameMap.walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        for (Food food : gameMap.foods) {
            g.drawImage(food.image, food.x, food.y, food.width, food.height, null);
        }
        for (Ghost ghost : gameMap.ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        

        
        if (gameOver) {
            g.setFont(images.pixelFont.deriveFont(Font.PLAIN, 30f));
            String text = "GAME OVER";
            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(text);
            g.drawString(text, (boardWidth - textWidth) / 2, boardHeight / 2);
        }
    }

    // Dipanggil tiap frame oleh Timer: jalanin animasi, gerakin semua objek, cek semua tabrakan
    private void move() {
        Player pacman = gameMap.player;
        pacman.tryApplyQueuedDirection(tileSize, gameMap.walls);
        pacman.animateTick(); // jalanin animasi kunyah pacman tiap frame

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // Cek pacman nabrak tembok
        for (Block wall : gameMap.walls) {
            if (Block.collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        // Efek tunnel: kalau pacman jalan sampai keluar sisi kiri/kanan papan
        // (cuma bisa kejadian di baris 'O', karena baris lain ketutup tembok)
        pacman.wrapAround(boardWidth);

        // Cek pacman nabrak hantu, sekalian gerakin (+ animasikan) tiap hantu
        for (Ghost ghost : gameMap.ghosts) {
            if (Block.collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }
            ghost.move(tileSize, boardWidth, gameMap.walls); // animasi hantu dijalankan di dalam move()
        }

        // Cek pacman makan makanan
        Food foodEaten = null;
        for (Food food : gameMap.foods) {
            if (Block.collision(pacman, food)) {
                foodEaten = food;
                score += 10;
                updateHighScoreIfNeeded();
            }
        }
        gameMap.foods.remove(foodEaten);

        // Kalau makanan habis, load ulang map (mulai level lagi)
        if (gameMap.foods.isEmpty()) {
            gameMap.load();
            resetPositions();
        }
    }

    // Kalau score sekarang udah lewatin high score, update + simpen ke file
    private void updateHighScoreIfNeeded() {
        if (score > highScore) {
            highScore = score;
            HighScoreStorage.save(highScore);
        }
    }

    private void resetPositions() {
        gameMap.player.reset();
        gameMap.player.velocityX = 0;
        gameMap.player.velocityY = 0;
        for (Ghost ghost : gameMap.ghosts) {
            ghost.reset();
            ghost.moveRandomDirection(tileSize, gameMap.walls);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
        handleMovementInput(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            gameMap.load();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        

        Player pacman = gameMap.player;

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U', tileSize, gameMap.walls);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D', tileSize, gameMap.walls);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L', tileSize, gameMap.walls);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R', tileSize, gameMap.walls);
        }
    }

    private void handleMovementInput(int keyCode) {
    Player pacman = gameMap.player;
    char direction = 0;

    if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
        direction = 'U';
    } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
        direction = 'D';
    } else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
        direction = 'L';
    } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
        direction = 'R';
    }

    if (direction != 0) {
        pacman.queueDirection(direction);
    }

    
}

}