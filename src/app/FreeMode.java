package app;

import core.MiniGame;
import core.Player;

import java.util.List;

// 담당: 나혜빈
// 게임 1개와 난이도를 직접 고르고, 끝나면 메뉴로 돌아옵니다.
public class FreeMode extends GameMode {
    @Override
    public String getLabel() {
        return "자유 모드";
    }

    @Override
    public void run(Player player, List<MiniGame> games) {
        // TODO
    }
}
