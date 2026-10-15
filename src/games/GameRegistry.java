package games;

import core.MiniGame;
import games.tictactoe.TicTacToe;

import java.util.List;

// 담당: 나혜빈
// 구체 게임 클래스 이름이 등장하는 유일한 곳. 게임을 추가할 때는 여기에 1줄만 추가합니다.
public final class GameRegistry {
    private GameRegistry() {
    }

    public static List<MiniGame> all() {
        return List.of(
            new NumberBaseball(),
            new CardMatch(),
            new MentalMath(),
            new MazeEscape(),
            new StroopTest(),
            new TicTacToe()
        );
    }
}
