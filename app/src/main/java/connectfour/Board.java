package connectfour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.primitives.Chars;

public class Board {
    public static final char EMPTY_FIELD = ' ';
    public static final char PLAYER_ONE_FIELD = 'x';
    public static final char PLAYER_TWO_FIELD = 'o';

    private static final Character[] LEGAL_FIELDS = { EMPTY_FIELD, PLAYER_ONE_FIELD, PLAYER_TWO_FIELD };

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

    public char[][] getFields() {
        return this.fields;
    }

    public boolean isLegalMove(int slot) {
        if (slot < 0 || slot >= this.cols) {
            return false;
        }
        char[] topRow = this.fields[0];
        char field = topRow[slot];
        return field == Board.EMPTY_FIELD;
    }

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