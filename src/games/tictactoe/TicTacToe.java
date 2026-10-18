package games.tictactoe;

import core.*;

import java.util.Map;

// 담당: 정종일

// 설명서: docs/games/tic_tac_toe.md
// 상대가 어떤 AI인지 모르고 ai.choose(board)만 부릅니다 (전략 패턴).
public class TicTacToe extends MiniGame {

    /*
    * player가 항상 선공
    * STANDARD_SECONDS : 기준 시간
    * ais = 난이도 -> AI 연결, getLabel() 값과 같게.
    * 다형성 : 값 타임은 AIStrategy 하나지만 실제로는 서로 다른 AI 3종이 들어가게.
    * */

    private static final char PLAYER = 'O';
    private static final char COMPUTER = 'X';
    private static final double STANDARD_SECONDS = 60.0;

    private final Map<String, AIStrategy> ais = Map.of(
            "Easy", new RandomAI(),
            "Normal", new BlockingAI(),
            "Hard", new MinimaxAI()
    );

    public TicTacToe() {
        super("삼목 대결");
    }

    @Override
    protected GameResult playRound(Difficulty difficulty) {
        // TODO
        // throw new UnsupportedOperationException("TODO");

        /*
        * isHard는 Hard로 equals로 비교
        * Hard는 비겨도 승리라서 boolean 값으로
        * */
        boolean isHard = difficulty.getLabel().equals("Hard");
        AIStrategy ai = ais.get(difficulty.getLabel());

        if (isHard) {
            System.out.println("비기기만 해도 성공");
        } else {
            System.out.println("컴퓨터를 이겨보세요.");
        }
        System.out.println();

        /*
        * 기본값
        * nanoTime : 두 시점의 차이로 경과 시간 재는 것
        * */

        char[] board = new char[9];
        java.util.Arrays.fill(board, BoardUtil.EMPTY);

        long start = System.nanoTime();
        printBoard(board);

        /*
        * 플레이어
        * place는 새 판 반환, board에 다시 대입
        * 컴퓨터
        * 어떤 AI인지 모르기 때문에 choose만 호출, 화면에 보여지게 +1
        * */
        char winner = BoardUtil.NO_WINNER;
        while (true) {
            int cell = readCell(board);
            board = BoardUtil.place(board, cell, PLAYER);
            winner = BoardUtil.winner(board);
            if (winner != BoardUtil.NO_WINNER || BoardUtil.isFull(board)) {
                printBoard(board);
                break;
            }

            int aiCell = ai.choose(board);
            board = BoardUtil.place(board, aiCell, COMPUTER);
            System.out.println("컴퓨터가 " + (aiCell + 1) + "번에 뒀습니다.");
            printBoard(board);
            winner = BoardUtil.winner(board);
            if (winner != BoardUtil.NO_WINNER || BoardUtil.isFull(board)) {
                break;
            }
        }

        /*
        * 나누는 수에 .0을 붙여야 소수점 버리지 않고 double
        * 무승부 : Hard는 무승부여도 성공이여서 삼항 연산자로 Hard랑 easy/noraml msg 분리
        * */

        double elapsed = (System.nanoTime() - start) / 1_000_000_000.0;

        boolean success;
        if (winner == PLAYER) {
            System.out.println(ConsoleUtil.color("승리했습니다!", ConsoleUtil.GREEN));
            success = true;
        } else if (winner == COMPUTER) {
            System.out.println(ConsoleUtil.color("컴퓨터가 이겼습니다.", ConsoleUtil.RED));
            success = false;
        } else {

            success = isHard;
            String msg = isHard ? "무승부 Hard에서는 성공입니다." : "무승부입니다. 이 난이도에서는 이겨야 성공입니다.";
            System.out.println(ConsoleUtil.color(msg, isHard ? ConsoleUtil.GREEN : ConsoleUtil.YELLOW));
        }

        return new GameResult(success, elapsed, STANDARD_SECONDS);
    }

    private int readCell(char[] board) {

        /*
        * 확인은 reandInt가 하고, 이미 놓인 칸인지 확인
        * continue : 이번 반복의 나머지를 건너뛰고 다시 입력
        * n-1 : 사람이 세는 번호를 배열 인덱스로 다시 바꾸게
        * */

        while (true) {
            int n = ConsoleUtil.readInt("플레이어의 차례 (1~9): ", 1, 9);
            if (board[n - 1] != BoardUtil.EMPTY) {
                System.out.println("이미 놓인 칸입니다.");
                continue;
            }
            return n - 1;
        }
    }

    /*
    * 화면 그리는 메서드
    * sep : 화면 사이 구분선
    * 화면이 세줄이라서 row 3번 돌게
    * 판이 1차원 배열이여서 계산해야함.
    *  i + 1, i + 2, i + 3, b[i], b[i + 1], b[i + 2]);
    * 여기서 왼쪽은 사람에게 보여주는 번호, 배열 인덱스는 +1
    * 오른쪽은 실제 말을 꺼내는 것 b는 char(글자) O, X
    * */

    private void printBoard(char[] b) {
        String sep = "-----------";
        System.out.println();
        for (int row = 0; row < 3; row++) {
            int i = row * 3;
            System.out.printf(" %d | %d | %d          %c | %c | %c%n",
                    i + 1, i + 2, i + 3, b[i], b[i + 1], b[i + 2]);
            if (row < 2) {
                System.out.println(sep + "        " + sep);
            }
        }
        System.out.println();
    }


    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new TicTacToe().play(new Player("테스트"), new NormalMode());
    }
}
