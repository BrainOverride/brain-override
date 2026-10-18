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

        //게임 시작 전 점수 저장
        int beforeScore = player.getScore();

        // GameResult 받기
        GameResult result = playRound(difficulty);

        if (result.isSuccess()) {
            player.addScore(difficulty.calcScore(result));
        } else {
            // 실패 처리는 난이도에 위임 (다형성). Easy면 재도전 결과가, 아니면 실패 결과가 돌아옴
            // → 여기서는 어떤 난이도인지 묻지 않음 (instanceof 없음)
            result = difficulty.onFail(player, this, result);
        }

        // 게임 전후 실제 점수 차이 계산
        int scoreDelta = player.getScore() - beforeScore;

        // 최종 성공 여부와 점수 변화 기록
        player.record(name, result.isSuccess(), scoreDelta);
    }

    // 게임마다 다른 부분: 자식이 반드시 구현
    protected abstract GameResult playRound(Difficulty difficulty);
}
