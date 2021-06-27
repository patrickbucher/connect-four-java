package connectfour;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void newDefaultBoardTest() {
        // Arrange/Given
        char[][] expectedDefaultFields = {
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_'}
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
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'}
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
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_', '_'}
        };

        Board board = Board.of(fields);

        assertArrayEquals(fields, board.getFields());
    }

    @Test
    void newBoardOfLegalFields() {
        char[][] fields = {
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', 'o', '_', '_', '_', '_', },
            {'_', '_', 'x', 'x', '_', '_', '_', },
            {'_', '_', 'x', 'o', 'o', '_', '_', },
            {'_', 'o', 'x', 'x', 'x', 'o', '_', }
        };

        Board board = Board.of(fields);

        assertArrayEquals(fields, board.getFields());
    }

    @Test
    void newBoardOfIllegalDimension() {
        char[][] fields = {
            {'_', '_', '_', },
            {'_', '_', '_', },
            {'_', '_', '_', }
        };

        assertThrows(IllegalArgumentException.class, () -> Board.of(fields));
    }

    @Test
    void newBoardOfIllegalFields() {
        char[][] fields = {
            {'_', '_', '_', '_'},
            {'_', '_', '_', '_'},
            {'_', '_', '_', '_'},
            {'_', '#', '_', '_'}
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
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', 'x', '_', '_', '_', }
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
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', 'x', '_', '_'},
            {'_', '_', '_', 'x', 'o', '_', '_'},
            {'_', 'o', 'x', 'x', 'o', '_', '_'}
        };
        char[][] expectedFieldsAfterMove = {
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', 'o', '_', '_'},
            {'_', '_', '_', '_', 'x', '_', '_'},
            {'_', '_', '_', 'x', 'o', '_', '_'},
            {'_', 'o', 'x', 'x', 'o', '_', '_'}
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
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', 'o', 'o', '_', '_', '_', },
            {'_', 'x', 'x', 'x', 'x', 'o', '_', },
        };
        Board board = Board.of(fields);
        int x = 2;
        int y = 5;

        assertTrue(board.isWinningSequenceAt(x, y));
    }

    @Test
    void isWinningSequenceVertically() {
        char[][] fields = {
            {'_', '_', '_', '_', '_', '_', '_' },
            {'_', '_', '_', 'o', '_', '_', '_' },
            {'_', '_', 'x', 'o', '_', '_', '_' },
            {'_', '_', 'x', 'o', 'o', '_', '_' },
            {'_', '_', 'x', 'o', 'x', '_', '_' },
            {'_', '_', 'o', 'x', 'x', '_', '_' }
        };
        Board board = Board.of(fields);
        int x = 3;
        int y = 4;

        assertTrue(board.isWinningSequenceAt(x, y));
    }

    @Test
    void isWinningSequenceDiagonalDown() {
        char[][] fields = {
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', 'o', '_', '_', '_', '_', '_', },
            {'_', 'x', 'o', '_', '_', '_', '_', },
            {'_', 'o', 'o', 'o', 'x', '_', '_', },
            {'_', 'x', 'x', 'x', 'o', 'x', '_', }
        };
        Board board = Board.of(fields);
        int x = 2;
        int y = 3;

        assertTrue(board.isWinningSequenceAt(x, y));
    }

    @Test
    void isWinningSequenceDiagonalUp() {
        char[][] fields = {
            {'_', '_', '_', '_', '_', '_', '_', },
            {'_', '_', '_', '_', '_', '_', 'o', },
            {'_', '_', '_', '_', 'x', 'o', 'o', },
            {'_', '_', '_', '_', 'o', 'x', 'x', },
            {'_', '_', '_', 'o', 'x', 'o', 'o', },
            {'_', '_', 'x', 'x', 'o', 'x', 'x', }
        };
        Board board = Board.of(fields);
        int x = 4;
        int y = 3;

        assertTrue(board.isWinningSequenceAt(x, y));
    }
}