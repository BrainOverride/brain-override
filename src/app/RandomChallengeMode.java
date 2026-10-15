package app;

import core.MiniGame;
import core.Player;

import java.util.List;

// 담당: 나혜빈
// 6개 중 3개를 중복 없이 랜덤 순서로 연속 출제. 난이도는 시작할 때 한 번 고릅니다.
public class RandomChallengeMode extends GameMode {
    private static final int GAME_COUNT = 3;

    @Override
    public String getLabel() {
        return "랜덤 챌린지";
    }

    @Override
    public void run(Player player, List<MiniGame> games) {
        // TODO
    }
}
