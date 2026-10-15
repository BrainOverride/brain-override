package core;

// 담당: 정종일
// 성공 100 × 1.5 + 시간 보너스(최대 50) / 실패 시 목숨 -1
public class NormalMode extends Difficulty {
    @Override
    public String getLabel() {
        return "Normal";
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
        return 200;
    }
}
