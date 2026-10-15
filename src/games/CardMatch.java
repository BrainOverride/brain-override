package games;

import core.Difficulty;
import core.GameResult;
import core.MiniGame;
import core.NormalMode;
import core.Player;

// 담당: 심소민
// 설명서: docs/games/card_match.md
public class CardMatch extends MiniGame {

    public CardMatch() {
        super("카드 뒤집기");
    }

    @Override
    protected GameResult playRound(Difficulty difficulty) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new CardMatch().play(new Player("테스트"), new NormalMode());
    }
}
