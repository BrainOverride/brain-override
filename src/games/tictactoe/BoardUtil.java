package games.tictactoe;

import java.util.ArrayList;
import java.util.List;

// 담당: 정종일
// TicTacToe와 AI 3종이 함께 쓰는 판 관련 기능. tictactoe 패키지 밖에서는 쓰지 않으므로 default 접근입니다.
final class BoardUtil {

    /*
    * empty : 빈칸
    * no_winner : 승자 없음 (char는 null을 못 담아서 'O','X',' '와 안 겹치는 글자로 표현)
    * int[][] : 2차원 배열
    * lines : 가로/세로/대각선 - 승리하는 8줄
    * */

    static final char EMPTY = ' ';
    static final char NO_WINNER = '-';

    private static final int[][] LINES = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    private BoardUtil() {
    }

    static char winner(char[] board) {
        // TODO 이기는 줄 8개를 int[][] 상수로 두고 확인
        // throw new UnsupportedOperationException("TODO");

        /*
        * 이길 수 있는 칸 3개를 line에 담아 8번 돌게
        * 첫 칸이 비어 있으면 안되고, 둘째,셋째도 첫 칸과 같다.
        * 이를 막지 않으면 게임 시작 시 게임 공백으로 스리를 하게된다.
        * */

        for (int[] line : LINES) {
            char a = board[line[0]];
            if (a != EMPTY && a == board[line[1]] && a == board[line[2]]) {
                return a;
            }
        }
        return NO_WINNER;
    }

    static List<Integer> emptyCells(char[] board) {
        // TODO
        // throw new UnsupportedOperationException("TODO");

        /*
        * 빈칸 번호 모아서 리스트로 반환
        * 빈 칸이 몇개인지 몰라서 크기가 변하는 List 사용
        * */

        List<Integer> cells = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == EMPTY) {
                cells.add(i);
            }
        }
        return cells;
    }

    static char[] place(char[] board, int cell, char mark) {
        // TODO
        // throw new UnsupportedOperationException("TODO");

        /*
        * 배열 참조 타입이라 char[] copy = board는 같은 배열 가리킨다.(복사 ㅌ)
        * clone()으로 진짜 복사본 만들어서 거기에 말 놓기.
        * 호출하는 쪽 : BoardUtil.place() 로 반환값 받으면 됨.
        * */

        char[] copy = board.clone();
        copy[cell] = mark;

        return copy;

    }

    static boolean isFull(char[] board) {
        // TODO
        // throw new UnsupportedOperationException("TODO");

        /*
        * winner()가 no_winner인데 isFull()이 true면 무승부
        * */

        return emptyCells(board).isEmpty();

    }
}
