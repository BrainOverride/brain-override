package core;

// 담당: 정종일
// 성공 100 × 2 + 시간 보너스 × 2(최대 100) / 실패 시 목숨 -1, 점수 -50
public class HardMode extends Difficulty {
    @Override
    public String getLabel() {
        return "Hard";
    }

    @Override
    public int calcScore(GameResult result) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void onFail(Player player, MiniGame game) {
        // TODO
    }

    @Override
    public int maxScore() {
        return 300;
    }
}
