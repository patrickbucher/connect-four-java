package connectfour;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void newDefaultBoardTest() {
        // Arrange/Given
        char[][] expectedDefaultFields = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' '}
        };

        // Act/When
        Board actualDefaultBoard = Board.newDefaultBoard();

        // Assert/Then
        assertArrayEquals(expectedDefaultFields, actualDefaultBoard.getFields());
    }

    @Test
    void new8Rows9ColsCustomBoardTest() {
        // Arrange/Given
        char[][] expected8Rows9ColsFields = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
        };

        // Act/When
        Board actual8Tows9ColsBoard = Board.newCustomBoard(8, 9);

        // Assert/Then
        assertArrayEquals(expected8Rows9ColsFields, actual8Tows9ColsBoard.getFields());
    }

    @Test
    void new3Rows5ColsCustomBoardFailTest() {
        assertThrows(IllegalArgumentException.class, () -> Board.newCustomBoard(3, 5));
    }

    @Test
    void newBoardOfEmptyFields() {
        char[][] fields = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
        };

        Board board = Board.of(fields);

        assertArrayEquals(fields, board.getFields());
    }

    @Test
    void newBoardOfLegalFields() {
        char[][] fields = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', 'o', ' ', ' ', ' ', ' ', },
            {' ', ' ', 'x', 'x', ' ', ' ', ' ', },
            {' ', ' ', 'x', 'o', 'o', ' ', ' ', },
            {' ', 'o', 'x', 'x', 'x', 'o', ' ', }
        };

        Board board = Board.of(fields);

        assertArrayEquals(fields, board.getFields());
    }

    @Test
    void newBoardOfIllegalDimension() {
        char[][] fields = {
            {' ', ' ', ' ', },
            {' ', ' ', ' ', },
            {' ', ' ', ' ', }
        };

        assertThrows(IllegalArgumentException.class, () -> Board.of(fields));
    }

    @Test
    void newBoardOfIllegalFields() {
        char[][] fields = {
            {' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' '},
            {' ', '#', ' ', ' '}
        };

        assertThrows(IllegalArgumentException.class, () -> Board.of(fields));
    }

    @Test
    void legalMoveOnEmptyBoard() {
        Board board = Board.newDefaultBoard();

        int slot = 3;
        var legal = board.isLegalMove(slot);

        assertTrue(legal);
    }

    @Test
    void illegalMoveOnEmptyBoard() {
        Board board = Board.newDefaultBoard();
        int slot = 317;

        assertFalse(board.isLegalMove(slot));
    }

    @Test
    void playLegalMoveOnEmptyBoard() {
        char[][] expected = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', 'x', ' ', ' ', ' ', }
        };
        Board board = Board.newDefaultBoard();
        var endedUpInRow = board.playMove(3, Board.PLAYER_ONE_FIELD);

        assertEquals(5, endedUpInRow);
        assertArrayEquals(expected, board.getFields());
    }

    @Test
    void playIllegalMoveOnEmptyBoard() {
        Board board = Board.newDefaultBoard();
        int slot = 453;
        char player = Board.PLAYER_ONE_FIELD;

        assertThrows(IllegalArgumentException.class, () -> board.playMove(slot, player));
    }

    @Test
    void playLegalMoveWithUnallowedPlayerOnEmptyBoard() {
        Board board = Board.newDefaultBoard();
        int slot = 3;
        char player = '#';

        assertThrows(IllegalArgumentException.class, () -> board.playMove(slot, player));
    }

    @Test
    void playLegalMoveOnNonEmptyBoard() {
        char[][] fieldsBeforeMove = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'x', ' ', ' '},
            {' ', ' ', ' ', 'x', 'o', ' ', ' '},
            {' ', 'o', 'x', 'x', 'o', ' ', ' '}
        };
        char[][] expectedFieldsAfterMove = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'o', ' ', ' '},
            {' ', ' ', ' ', ' ', 'x', ' ', ' '},
            {' ', ' ', ' ', 'x', 'o', ' ', ' '},
            {' ', 'o', 'x', 'x', 'o', ' ', ' '}
        };

        Board board = Board.of(fieldsBeforeMove);
        var endedUpInRow = board.playMove(4, Board.PLAYER_TWO_FIELD);

        assertEquals(2, endedUpInRow);
        assertArrayEquals(expectedFieldsAfterMove, board.getFields());
    }

    @Test
    void noWinningSequenceOnEmptyBoard() {
        Board board = Board.newDefaultBoard();

        assertFalse(board.isWinningSequenceAt(3, 3));
    }

    @Test
    void isWinningSequenceHorizontally() {
        char[][] fields = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', 'o', 'o', ' ', ' ', ' ', },
            {' ', 'x', 'x', 'x', 'x', 'o', ' ', },
        };
        Board board = Board.of(fields);
        int x = 2;
        int y = 5;

        assertTrue(board.isWinningSequenceAt(x, y));
    }

    @Test
    void isWinningSequenceVertically() {
        char[][] fields = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ' },
            {' ', ' ', ' ', 'o', ' ', ' ', ' ' },
            {' ', ' ', 'x', 'o', ' ', ' ', ' ' },
            {' ', ' ', 'x', 'o', 'o', ' ', ' ' },
            {' ', ' ', 'x', 'o', 'x', ' ', ' ' },
            {' ', ' ', 'o', 'x', 'x', ' ', ' ' }
        };
        Board board = Board.of(fields);
        int x = 3;
        int y = 4;

        assertTrue(board.isWinningSequenceAt(x, y));
    }

    @Test
    void isWinningSequenceDiagonalDown() {
        char[][] fields = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', 'o', ' ', ' ', ' ', ' ', ' ', },
            {' ', 'x', 'o', ' ', ' ', ' ', ' ', },
            {' ', 'o', 'o', 'o', 'x', ' ', ' ', },
            {' ', 'x', 'x', 'x', 'o', 'x', ' ', }
        };
        Board board = Board.of(fields);
        int x = 2;
        int y = 3;

        assertTrue(board.isWinningSequenceAt(x, y));
    }

    @Test
    void isWinningSequenceDiagonalUp() {
        char[][] fields = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            {' ', ' ', ' ', ' ', ' ', ' ', 'o', },
            {' ', ' ', ' ', ' ', 'x', 'o', 'o', },
            {' ', ' ', ' ', ' ', 'o', 'x', 'x', },
            {' ', ' ', ' ', 'o', 'x', 'o', 'o', },
            {' ', ' ', 'x', 'x', 'o', 'x', 'x', }
        };
        Board board = Board.of(fields);
        int x = 4;
        int y = 3;

        assertTrue(board.isWinningSequenceAt(x, y));
    }
}