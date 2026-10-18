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
//        throw new UnsupportedOperationException("TODO");
        if (!result.isSuccess()) {
            return 0;
        }

        int base = (int) (result.getBaseScore() * 1.5);
        double ratio = 1.0 - result.getElapsedSeconds() / result.getStandardSeconds();
        // int bonus = (int) Math.max(0, Math.min(50, ratio * 50));
        int bonus = (int) Math.clamp(ratio * 50, 0, 50);

        return base + bonus;
    }

    @Override
    public GameResult onFail(Player player, MiniGame game, GameResult failed) {
        player.loseLife();
        return failed;   // 재도전 없음 → 실패 결과가 최종 결과
    }

    @Override
    public int maxScore() {
        return 200;
    }
}
