import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
 */
public class GameBoard extends JPanel implements ActionListener, KeyListener {
    private int tileSize = 32;
    private int boardWidth = GameMap.COLUMN_COUNT * tileSize;
    private int boardHeight = GameMap.ROW_COUNT * tileSize;

    private GameImages images;
    private GameMap gameMap;

    private Timer gameLoop;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    public GameBoard() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

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
        Player pacman = gameMap.player;
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Ghost ghost : gameMap.ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : gameMap.walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Food food : gameMap.foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize / 2, tileSize / 2);
        } else {
            g.drawString("x" + lives + " Score: " + score, tileSize / 2, tileSize / 2);
        }
    }

    // Dipanggil tiap frame oleh Timer: jalanin animasi, gerakin semua objek, cek semua tabrakan
    private void move() {
        Player pacman = gameMap.player;
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
            }
        }
        gameMap.foods.remove(foodEaten);

        // Kalau makanan habis, load ulang map (mulai level lagi)
        if (gameMap.foods.isEmpty()) {
            gameMap.load();
            resetPositions();
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
    public void keyPressed(KeyEvent e) {}

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
}