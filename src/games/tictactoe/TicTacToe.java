package games.tictactoe;

import core.Difficulty;
import core.GameResult;
import core.MiniGame;
import core.NormalMode;
import core.Player;

// 담당: 정종일
// 설명서: docs/games/tic_tac_toe.md
// 상대가 어떤 AI인지 모르고 ai.choose(board)만 부릅니다 (전략 패턴).
public class TicTacToe extends MiniGame {

    public TicTacToe() {
        super("삼목 대결");
    }

    @Override
    protected GameResult playRound(Difficulty difficulty) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new TicTacToe().play(new Player("테스트"), new NormalMode());
    }
}
