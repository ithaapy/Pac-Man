/**
 * Food = titik makanan kecil. Sama kayak Wall, cuma diam di tempat.
 * Gambarnya di-null karena digambar manual pakai fillRect() di GameBoard,
 * bukan pakai Image.
 */
public class Food extends Block {
    public Food(int x, int y, int width, int height) {
        super(null, x, y, width, height);
    }
}