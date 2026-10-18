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

        // 3. Easy 난이도에서 실패한 경우 재도전
        if (!result.isSuccess() && difficulty instanceof EasyMode) {

            // 재도전 결과를 최종 결과로 저장
            result = ((EasyMode) difficulty).retry(this);

            if (result.isSuccess()) {
                player.addScore(difficulty.calcScore(result));
            } else {
                player.loseLife();
            }

        } else if (result.isSuccess()) {

            // 첫 번째 게임에서 성공
            player.addScore(difficulty.calcScore(result));

        } else {

            // Normal / Hard 실패 처리
            difficulty.onFail(player, this);
        }

        // 게임 전후 실제 점수 차이 계산
        int scoreDelta = player.getScore() - beforeScore;

        // 최종 성공 여부와 점수 변화 기록
        player.record(name, result.isSuccess(), scoreDelta);
    }

    // 게임마다 다른 부분: 자식이 반드시 구현
    protected abstract GameResult playRound(Difficulty difficulty);
}
