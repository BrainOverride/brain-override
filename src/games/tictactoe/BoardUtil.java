package games.tictactoe;

import java.util.List;

// 담당: 정종일
// TicTacToe와 AI 3종이 함께 쓰는 판 관련 기능. tictactoe 패키지 밖에서는 쓰지 않으므로 default 접근입니다.
final class BoardUtil {
    static final char EMPTY = ' ';
    static final char NO_WINNER = '-';

    private BoardUtil() {
    }

    // 'O', 'X', 또는 NO_WINNER
    static char winner(char[] board) {
        // TODO 이기는 줄 8개를 int[][] 상수로 두고 확인
        throw new UnsupportedOperationException("TODO");
    }

    static List<Integer> emptyCells(char[] board) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // 원본은 그대로 두고 복사본(board.clone())에 수를 둔 새 판을 반환
    static char[] place(char[] board, int cell, char mark) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    static boolean isFull(char[] board) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
