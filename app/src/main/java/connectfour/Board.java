package connectfour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.primitives.Chars;

/**
 * Board represents a game board for Connect Four (Four in a Row).
 */
public class Board {
    /** empty (unplayed) field on the board */
    public static final char EMPTY_FIELD = '_';

    /** player one's field on the board */
    public static final char PLAYER_ONE_FIELD = 'x';

    /** player two's field on the board */
    public static final char PLAYER_TWO_FIELD = 'o';

    private static final Character[] LEGAL_FIELDS = { EMPTY_FIELD, PLAYER_ONE_FIELD, PLAYER_TWO_FIELD };

    private final int rows;
    private final int cols;
    private final char[][] fields;

    /**
     * Creates a new board with the default dimensions of 6 rows and 7 columns.
     * 
     * @return a Board instance of default dimensions
     */
    public static Board newDefaultBoard() {
        return new Board(6, 7);
    }

    /**
     * Creates a board with custom dimensions. The board must be at least of size
     * four in every dimension.
     * 
     * @param rows number of rows
     * @param cols number of columns
     * @return a board of the given dimensions
     */
    public static Board newCustomBoard(int rows, int cols) {
        if (rows < 4 || cols < 4) {
            throw new IllegalArgumentException("A board needs at least four rows and columns.");
        }
        return new Board(rows, cols);
    }

    /**
     * Creates a Board instance from the given two-dimensional fields array. The
     * fields either must be empty or be from player one or two. The minimum
     * dimensions are 4x4.
     * 
     * @param fields two-dimensional array consisting of empty and player one/two
     *               fields
     * @return a Board instance with the given fields
     */
    public static Board of(char[][] fields) {
        if (fields.length < 4 || fields[0].length < 4) {
            throw new IllegalArgumentException("A board needs at least four rows and columns.");
        }
        for (var r = 0; r < fields.length; r++) {
            for (var c = 0; c < fields[0].length; c++) {
                final char currentField = fields[r][c];
                boolean legal = Arrays.stream(LEGAL_FIELDS).anyMatch(x -> currentField == x);
                if (!legal) {
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

    /**
     * Returns the board's underlying fields as a two-dimensional array. The rows
     * are the first dimension, the columns are the second dimension.
     * 
     * @return a two-dimensional array representing the board
     */
    public char[][] getFields() {
        return this.fields;
    }

    /**
     * Checks whether or not a move is allowed in the given slot.
     * 
     * @param slot a zero-based column index
     * @return a boolean indicating whether or not the move is legal
     */
    public boolean isLegalMove(int slot) {
        if (slot < 0 || slot >= this.cols) {
            return false;
        }
        char[] topRow = this.fields[0];
        char field = topRow[slot];
        return field == Board.EMPTY_FIELD;
    }

    /**
     * Let's the given player's stone drop into the given slot.
     * 
     * @param slot   a zero-based column index
     * @param player the symbol for either player one or two
     * @return the row (zero-based horizontal index) to which the stone dropped
     */
    public int playMove(int slot, char player) {
        if (!isLegalMove(slot)) {
            var error = String.format("Move in slot %d is not a legal move.", slot);
            throw new IllegalArgumentException(error);
        }
        if (player != Board.PLAYER_ONE_FIELD && player != Board.PLAYER_TWO_FIELD) {
            var error = String.format("The player char %c is not allowed.", player);
            throw new IllegalArgumentException(error);
        }
        for (var r = this.rows - 1; r >= 0; r--) {
            if (this.fields[r][slot] == Board.EMPTY_FIELD) {
                this.fields[r][slot] = player;
                return r;
            }
        }
        var error = String.format("Unable to find empty field in slot %d.", slot);
        throw new IllegalStateException(error);
    }

    /**
     * Checks if the stone played in slot x that dropped to row y is part of a
     * winning sequence, i.e. four of the same fields in a row.
     * 
     * @param x the slot a stone was dropped into
     * @param y the row a stone has fallen down to
     * @return a boolean indicating wether or not the given indices are part of a
     *         winning sequence
     */
    public boolean isWinningSequenceAt(int x, int y) {
        char field = this.fields[y][x];
        if (field != Board.PLAYER_ONE_FIELD && field != Board.PLAYER_TWO_FIELD) {
            return false;
        }

        List<Character> winningSequence = Arrays.asList(field, field, field, field);

        List<Character> row = Chars.asList(this.fields[y]);
        List<Character> column = this.getColumn(x);
        List<Character> diagonalUp = this.getDiagonalUp(x, y);
        List<Character> diagonalDown = this.getDiagonalDown(x, y);

        return isContained(winningSequence, row) || isContained(winningSequence, column)
                || isContained(winningSequence, diagonalUp) || isContained(winningSequence, diagonalDown);
    }

    private List<Character> getColumn(int x) {
        List<Character> column = new ArrayList<>();
        for (var r = 0; r < this.rows; r++) {
            column.add(this.fields[r][x]);
        }
        return column;
    }

    private List<Character> getDiagonalUp(int x, int y) {
        List<Character> rightUpwards = new ArrayList<>();
        List<Character> leftDownwards = new ArrayList<>();

        // from the center to the upper right
        for (int r = y, c = x; r >= 0 && c < this.cols; r--, c++) {
            rightUpwards.add(this.fields[r][c]);
        }

        // one left below from the center to the lower left
        for (int r = y + 1, c = x - 1; r < this.rows && c >= 0; r++, c--) {
            leftDownwards.add(this.fields[r][c]);
        }
        Collections.reverse(leftDownwards);

        return Stream.concat(leftDownwards.stream(), rightUpwards.stream()).collect(Collectors.toList());
    }

    private List<Character> getDiagonalDown(int x, int y) {
        List<Character> leftUpwards = new ArrayList<>();
        List<Character> rightDownards = new ArrayList<>();

        // from the center to the upper left
        for (int r = y, c = x; r >= 0 && c >= 0; r--, c--) {
            leftUpwards.add(this.fields[r][c]);
        }
        Collections.reverse(leftUpwards);

        // one right below from the center to the lower left
        for (int r = y + 1, c = x + 1; r < this.rows && c < this.cols; r++, c++) {
            rightDownards.add(this.fields[r][c]);
        }

        return Stream.concat(leftUpwards.stream(), rightDownards.stream()).collect(Collectors.toList());
    }

    private static boolean isContained(List<Character> subSequence, List<Character> sequence) {
        return Collections.indexOfSubList(sequence, subSequence) != -1;
    }
}