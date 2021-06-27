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

}