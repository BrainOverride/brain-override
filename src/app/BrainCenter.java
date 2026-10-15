package app;

import core.MiniGame;
import games.GameRegistry;

import java.util.List;
import java.util.Map;

// 담당: 나혜빈
// 메인 메뉴. 구체 게임 이름을 모르고 List<MiniGame>만 다룹니다.
public class BrainCenter {
    private final List<MiniGame> games = GameRegistry.all();
    private final Map<Integer, GameMode> modes = Map.of(
        1, new FreeMode(),
        2, new RandomChallengeMode()
    );

    public void start() {
        // TODO 이름 입력 → 모드 선택 → modes.get(choice).run(player, games) → 결과 화면(총점, 두뇌 나이)
    }
}
