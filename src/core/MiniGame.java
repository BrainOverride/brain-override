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
        // 게임 이름과 난이도 안내 출력
        System.out.println("=== " + name + " (" + difficulty.getLabel() + ") ===");

        // GameResult 받기
        GameResult result = playRound(difficulty);

        // 게임 성공 여부에 따라 분기
        if (result.isSuccess()) { // 게임 성공이면
            player.addScore(difficulty.calcScore(result));
        } else {
            difficulty.onFail(player, this);
        }
        int beforeScore = player.getScore();

        // 기존 게임 실행 및 점수 처리 코드
        int scoreDelta = player.getScore() - beforeScore;
        player.record(name, result.isSuccess(), scoreDelta);
    }

    // 게임마다 다른 부분: 자식이 반드시 구현
    protected abstract GameResult playRound(Difficulty difficulty);
}
