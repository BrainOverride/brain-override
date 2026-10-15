package games;

import core.Difficulty;
import core.GameResult;
import core.MiniGame;
import core.NormalMode;
import core.Player;

// 담당: 심소민
// 설명서: docs/games/stroop_test.md
public class StroopTest extends MiniGame {

    public StroopTest() {
        super("스트룹 테스트");
    }

    @Override
    protected GameResult playRound(Difficulty difficulty) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new StroopTest().play(new Player("테스트"), new NormalMode());
    }
}
