package connectfour;

public class Board {
    public static final char EMPTY_FIELD = ' ';
    public static final char PLAYER_ONE_FIELD = 'x';
    public static final char PLAYER_TWO_FIELD = 'o';

    private final int rows;
    private final int cols;
    private final char[][] fields;

    public static Board newDefaultBoard() {
        return new Board(6, 7);
    }

    private Board(int rows, int cols) {
        if (rows < 4 || cols < 4) {
            throw new IllegalArgumentException("A board needs at least four rows and columns.");
        }
        this.rows = rows;
        this.cols = cols;
        this.fields = new char[rows][];
        for (int r = 0; r < this.rows; r++) {
            this.fields[r] = new char[cols];
            for (int c = 0; c < this.cols; c++) {
                this.fields[r][c] = EMPTY_FIELD;
            }
        }
    }

    public char[][] getFields() {
        return this.fields;
    }

}