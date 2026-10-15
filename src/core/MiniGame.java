package core;

// 담당: 심소민
// 모든 미니게임의 부모. 공통 흐름(play)은 여기서 한 번만 쓰고, 게임마다 다른 부분(playRound)만 자식이 구현합니다.
public abstract class MiniGame {
    private final String name;

    protected MiniGame(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 템플릿 메서드: 모든 게임의 공통 흐름. final이라 자식이 바꿀 수 없음
    public final void play(Player player, Difficulty difficulty) {
        // TODO 1. 게임 이름과 난이도 안내 출력
        // TODO 2. playRound(difficulty) 호출 → GameResult 받기
        // TODO 3. 성공이면 player.addScore(difficulty.calcScore(result))
        //         실패면 difficulty.onFail(player, this)
        // TODO 4. player.record(name, result)
    }

    // 게임마다 다른 부분: 자식이 반드시 구현
    protected abstract GameResult playRound(Difficulty difficulty);
}
