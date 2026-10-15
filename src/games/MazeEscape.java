package games;

import core.Difficulty;
import core.GameResult;
import core.MiniGame;
import core.NormalMode;
import core.Player;

// 담당: 나혜빈
// 설명서: docs/games/maze_escape.md
public class MazeEscape extends MiniGame {

    public MazeEscape() {
        super("미로 탈출");
    }

    @Override
    protected GameResult playRound(Difficulty difficulty) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new MazeEscape().play(new Player("테스트"), new NormalMode());
    }
}
