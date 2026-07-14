import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel implements ActionListener, KeyListener {


    private GameState state = GameState.MENU;


    private final List<UiButton> menuButtons = new ArrayList<>();
    private final List<UiButton> pauseButtons = new ArrayList<>();
    private final List<UiButton> gameOverButtons = new ArrayList<>();
    private final List<UiButton> winButtons = new ArrayList<>();

    private static final Color BORDER_COLOR = new Color(70, 58, 115);
    private static final Color BACKGROUND_COLOR = new Color(42, 40, 58);
    private static final int BORDER = 16;
    private static final int HEADER_HEIGHT = 70;
    private static final int FOOTER_HEIGHT = 50;
    private static final int LIFE_ICON_SIZE = 24;
    private static final int LIFE_ICON_GAP = 8;
    private static final int BUTTON_SIZE = 48;

    private final int tileSize = 32;
    private final int boardWidth = GameMap.COLUMN_COUNT * tileSize;
    private final int boardHeight = GameMap.ROW_COUNT * tileSize;
    private final int totalWidth = boardWidth + BORDER * 2;
    private final int totalHeight = boardHeight + HEADER_HEIGHT + FOOTER_HEIGHT + BORDER * 2;

    private GameImages images;
    private GameMap gameMap;
    private Timer gameLoop;

    private int score = 0;
    private int highScore;
    private int lives = 3;

    public GameBoard() {
        setPreferredSize(new Dimension(totalWidth, totalHeight));
        setBackground(BORDER_COLOR);
        addKeyListener(this);
        setFocusable(true);

        highScore = HighScoreStorage.load();
        images = new GameImages(getClass());
        gameMap = new GameMap(images, tileSize);

        initButtons();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

        gameLoop = new Timer(50, this);
    }

    // ===================== DRAW =====================

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        switch (state) {
            case MENU -> drawMenu(g);
            case PLAYING -> drawPlaying(g);
            case PAUSED -> {
                drawPlaying(g);
                drawPauseOverlay(g);
            }
            case GAME_OVER -> {
                drawPlaying(g);
                drawGameOverOverlay(g);
            }
            case WIN -> {
                drawPlaying(g);
                drawWinOverlay(g);
            }
        }
    }

    // #4 — helper scale Bg3.png (ADA DI FILE INI: GameBoard.java)
    private void drawScaledCentered(Graphics g, BufferedImage img, int panelW, int panelH) {
        double scale = Math.min((double) panelW / img.getWidth(),
                                (double) panelH / img.getHeight());
        int w = (int) (img.getWidth() * scale);
        int h = (int) (img.getHeight() * scale);
        int x = (panelW - w) / 2;
        int y = (panelH - h) / 2;
        g.drawImage(img, x, y, w, h, null);
    }

    private Rectangle getCenteredPanelRect() {
        BufferedImage bg = images.panelBackground;
        int maxW = 420;
        int maxH = 560;
        double scale = Math.min((double) maxW / bg.getWidth(),
                                (double) maxH / bg.getHeight());
        int w = (int) (bg.getWidth() * scale);
        int h = (int) (bg.getHeight() * scale);
        int x = (totalWidth - w) / 2;
        int y = (totalHeight - h) / 2;
        return new Rectangle(x, y, w, h);
    }

    private void drawDarkOverlay(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, totalWidth, totalHeight);
    }

    private void drawPanelBackground(Graphics g) {
    Rectangle panel = getCenteredPanelRect();
    Graphics pg = g.create(panel.x, panel.y, panel.width, panel.height);
    drawScaledCentered(pg, images.panelBackground, panel.width, panel.height);
    pg.dispose();
    }

    private void drawCenteredText(Graphics g, String text, int centerY, float size) {
        Font font = images.pixelFont.deriveFont(Font.PLAIN, size);
        g.setFont(font);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int x = (totalWidth - fm.stringWidth(text)) / 2;
        g.drawString(text, x, centerY);
    }

    private void drawButtons(Graphics g, List<UiButton> buttons) {
        Font font = images.pixelFont.deriveFont(Font.PLAIN, 18f);
        g.setColor(Color.WHITE);
        for (UiButton button : buttons) {
            button.draw(g, font);
        }
    }

    // ---------- MENU ----------
    private void drawMenu(Graphics g) {
        drawScaledCentered(g, images.panelBackground, totalWidth, totalHeight);

        int logoSize = 160;
        int logoX = (totalWidth - logoSize) / 2;
        int logoY = totalHeight / 2 - 220;
        g.drawImage(images.logoImage, logoX, logoY, logoSize, logoSize, null);

        drawCenteredText(g, "PAC-MAN", logoY + logoSize + 40, 32f);
        drawButtons(g, menuButtons);
    }

    // ---------- PLAYING ----------
    private void drawPlaying(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(BORDER, BORDER, totalWidth - BORDER * 2, totalHeight - BORDER * 2);

        drawHeader(g);

        Graphics mazeGraphics = g.create(BORDER, BORDER + HEADER_HEIGHT, boardWidth, boardHeight);
        drawMaze(mazeGraphics);
        mazeGraphics.dispose();

        drawFooter(g);
    }

    private void drawHeader(Graphics g) {
        Font labelFont = images.pixelFont.deriveFont(Font.PLAIN, 14f);
        Font valueFont = images.pixelFont.deriveFont(Font.PLAIN, 20f);
        Font titleFont = images.pixelFont.deriveFont(Font.PLAIN, 22f);

        g.setColor(Color.WHITE);

        g.setFont(labelFont);
        g.drawString("Your Score", BORDER + 10, BORDER + 24);
        g.setFont(valueFont);
        g.drawString(String.valueOf(score), BORDER + 10, BORDER + 50);

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

        g.setFont(titleFont);
        FontMetrics titleMetrics = g.getFontMetrics();
        String title = "Pac-Man";
        int titleWidth = titleMetrics.stringWidth(title);
        int titleX = (totalWidth - titleWidth) / 2;
        int titleY = BORDER + (HEADER_HEIGHT + titleMetrics.getAscent()) / 2 - 4;
        g.drawString(title, titleX, titleY);
    }

    private void drawFooter(Graphics g) {
    int footerTop = BORDER + HEADER_HEIGHT + boardHeight;
    int startX = BORDER + 10;
    int iconY = footerTop + (FOOTER_HEIGHT - LIFE_ICON_SIZE) / 2;

    for (int i = 0; i < lives; i++) {
        int iconX = startX + i * (LIFE_ICON_SIZE + LIFE_ICON_GAP);
        g.drawImage(images.pacIconImage, iconX, iconY, LIFE_ICON_SIZE, LIFE_ICON_SIZE, null);
    }
    }

    private void drawMaze(Graphics g) {
        if (gameMap.player == null) {
            return;
        }

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
    }

    // ---------- PAUSED ----------
    private void drawPauseOverlay(Graphics g) {
        drawDarkOverlay(g);
        drawPanelBackground(g);

        Rectangle panel = getCenteredPanelRect();
        int centerY = panel.y + panel.height / 2;

        drawCenteredText(g, "PAUSE", centerY - 55, 30f);
        drawButtons(g, pauseButtons);
    }

    // ---------- GAME OVER ----------
    private void drawGameOverOverlay(Graphics g) {
        drawDarkOverlay(g);
        drawPanelBackground(g);

        Rectangle panel = getCenteredPanelRect();
        int centerY = panel.y + panel.height / 2;

        drawCenteredText(g, "GAME OVER", centerY - 100, 30f);
        drawCenteredText(g, "Score: " + score, centerY - 60, 18f);
        drawCenteredText(g, "High Score: " + highScore, centerY - 30, 18f);
        drawButtons(g, gameOverButtons);
    }

    
    private void drawWinOverlay(Graphics g) {
    drawDarkOverlay(g);
    drawPanelBackground(g);

    Rectangle panel = getCenteredPanelRect();
    int centerY = panel.y + panel.height / 2;

    drawCenteredText(g, "YOU WIN!", centerY - 100, 30f);   // judul besar, sama GAME OVER
    drawCenteredText(g, "Score: " + score, centerY - 60, 18f);
    drawCenteredText(g, "High Score: " + highScore, centerY - 30, 18f);
    drawButtons(g, winButtons);
    }

    // ===================== BUTTON INIT =====================

    private void initButtons() {
    menuButtons.clear();
    pauseButtons.clear();
    gameOverButtons.clear();
    winButtons.clear();

    Rectangle panel = getCenteredPanelRect();
    int cx = panel.x + panel.width / 2;
    int cy = panel.y + panel.height / 2;
    int btn = BUTTON_SIZE;
    int gap = 16;

    // MENU: hanya Start
    menuButtons.add(new UiButton(images.buttonStartFrames,
            new Rectangle(cx - btn / 2, cy + 20, btn, btn), this::startNewGame));

    // PAUSED: Home (kiri) - Start/Resume (tengah) - Restart (kanan), sejajar 1 baris,
    // dekat dengan text "PAUSE" (lihat drawPauseOverlay -> centerY - 55)
    int pauseRowY = cy - 5;
    int pauseRowLeftX = cx - (3 * btn + 2 * gap) / 2;
    pauseButtons.add(new UiButton(images.buttonHomeFrames,
            new Rectangle(pauseRowLeftX, pauseRowY, btn, btn), this::goToMenu));
    pauseButtons.add(new UiButton(images.buttonStartFrames,
            new Rectangle(pauseRowLeftX + btn + gap, pauseRowY, btn, btn), this::resumeGame));
    pauseButtons.add(new UiButton(images.buttonRestartFrames,
            new Rectangle(pauseRowLeftX + 2 * (btn + gap), pauseRowY, btn, btn), this::restartGame));

    // GAME OVER: Home (kiri) - Restart (kanan)
    int gameOverRowY = cy + 10;
    gameOverButtons.add(new UiButton(images.buttonHomeFrames,
            new Rectangle(cx - btn - gap / 2, gameOverRowY, btn, btn), this::goToMenu));
    gameOverButtons.add(new UiButton(images.buttonRestartFrames,
            new Rectangle(cx + gap / 2, gameOverRowY, btn, btn), this::restartGame));

    // WIN: Home (kiri) - Restart (kanan)
    int winRowY = cy + 10;
    winButtons.add(new UiButton(images.buttonHomeFrames,
            new Rectangle(cx - btn - gap / 2, winRowY, btn, btn), this::goToMenu));
    winButtons.add(new UiButton(images.buttonRestartFrames,
            new Rectangle(cx + gap / 2, winRowY, btn, btn), this::restartGame));
    }

    private void handleMouseClick(int x, int y) {
        List<UiButton> buttons = switch (state) {
            case MENU -> menuButtons;
            case PAUSED -> pauseButtons;
            case GAME_OVER -> gameOverButtons;
            case WIN -> winButtons;
            default -> List.of();
        };

        for (UiButton button : buttons) {
            if (button.contains(x, y)) {
                button.click();
                repaint();

                Timer repaintTimer = new Timer(50, e -> repaint());
                repaintTimer.start();

                Timer stopTimer = new Timer(250, e -> {
                    repaintTimer.stop();
                    ((Timer) e.getSource()).stop();
                });
                stopTimer.setRepeats(false);
                stopTimer.start();
                return;
            }
        }
    }

    // ===================== STATE TRANSITIONS =====================

    private void startNewGame() {
        score = 0;
        lives = 3;
        loadGame();
        state = GameState.PLAYING;
        gameLoop.start();
        requestFocusInWindow();
        repaint();
    }

    private void restartGame() {
        score = 0;
        lives = 3;
        loadGame();
        state = GameState.PLAYING;
        gameLoop.start();
        requestFocusInWindow();
        repaint();
    }

    private void resumeGame() {
        state = GameState.PLAYING;
        gameLoop.start();
        requestFocusInWindow();
        repaint();
    }

    private void goToMenu() {
        state = GameState.MENU;
        gameLoop.stop();
        requestFocusInWindow();
        repaint();
    }


    private void loadGame() {
        gameMap.load();
        resetPositions();
        for (Ghost ghost : gameMap.ghosts) {
            ghost.moveRandomDirection(tileSize, gameMap.walls);
        }
    }



    // ===================== GAME LOOP =====================

    private void move() {
        Player pacman = gameMap.player;
        pacman.tryApplyQueuedDirection(tileSize, gameMap.walls);
        pacman.animateTick();

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        for (Block wall : gameMap.walls) {
            if (Block.collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        pacman.wrapAround(boardWidth);

        for (Ghost ghost : gameMap.ghosts) {
            if (Block.collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    state = GameState.GAME_OVER;
                    gameLoop.stop();
                    return;
                }
                resetPositions();
            }
            ghost.move(tileSize, boardWidth, gameMap.walls);
        }

        Food foodEaten = null;
        for (Food food : gameMap.foods) {
            if (Block.collision(pacman, food)) {
                foodEaten = food;
                score += 10;
                updateHighScoreIfNeeded();
            }
        }
        gameMap.foods.remove(foodEaten);

        // Menang jika semua makanan habis → tampilkan panel WIN
        if (gameMap.foods.isEmpty()) {
            updateHighScoreIfNeeded();
            state = GameState.WIN;
            gameLoop.stop();
        }
    }
    

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
        if (state != GameState.PLAYING) {
            return;
        }
        move();
        repaint();
    }

    // ===================== KEYBOARD (#7) =====================

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (state == GameState.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                state = GameState.PAUSED;
                gameLoop.stop();
                repaint();
                return;
            }
            handleMovementInput(e.getKeyCode());
        } else if (state == GameState.PAUSED) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                resumeGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

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