import javax.swing.JFrame;

/**
 * Main = entry point. Tugasnya cuma bikin window (JFrame) dan
 * pasang GameBoard di dalamnya, lalu tampilkan.
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pac Man");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameBoard gameBoard = new GameBoard();
        frame.add(gameBoard);
        frame.pack(); // ukuran window otomatis ngikutin ukuran GameBoard
        frame.setVisible(true);
        gameBoard.requestFocus(); // biar keyboard langsung kepencet ke game
    }
}