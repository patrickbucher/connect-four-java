package connectfour;

import java.util.Arrays;

public class Board {
    public static final char EMPTY_FIELD = ' ';
    public static final char PLAYER_ONE_FIELD = 'x';
    public static final char PLAYER_TWO_FIELD = 'o';

    private static final Character[] LEGAL_FIELDS = {EMPTY_FIELD, PLAYER_ONE_FIELD, PLAYER_TWO_FIELD};

    private final int rows;
    private final int cols;
    private final char[][] fields;

    public static Board newDefaultBoard() {
        return new Board(6, 7);
    }

    public static Board newCustomBoard(int rows, int cols) {
        if (rows < 4 || cols < 4) {
            throw new IllegalArgumentException("A board needs at least four rows and columns.");
        }
        return new Board(rows, cols);
    }

    public static Board of(char[][] fields) {
        if (fields.length < 4 || fields[0].length < 4) {
            throw new IllegalArgumentException("A board needs at least four rows and columns.");
        }
        for (var r = 0; r < fields.length; r++) {
            for (var c = 0; c < fields[0].length; c++) {
                final char currentField = fields[r][c];
                boolean legal = Arrays.stream(LEGAL_FIELDS).anyMatch(x -> currentField == x);
                if  (!legal) {
                    var error = String.format("Illegal field '%c' at index (%d,%d).", currentField, r, c);
                    throw new IllegalArgumentException(error);
                }
            }
        }
        return new Board(fields);
    }

    private Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.fields = new char[rows][];
        for (var r = 0; r < this.rows; r++) {
            this.fields[r] = new char[cols];
            for (var c = 0; c < this.cols; c++) {
                this.fields[r][c] = EMPTY_FIELD;
            }
        }
    }

    private Board(char[][] fields) {
        this.fields = fields;
        this.rows = fields.length;
        this.cols = fields[0].length;
    }

    public char[][] getFields() {
        return this.fields;
    }

}