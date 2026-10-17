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
//        throw new UnsupportedOperationException("TODO");
        if (!result.isSuccess()) {
            return 0;
        }
        int base = result.getBaseScore() * 2;
        double ratio = 1.0 - result.getElapsedSeconds() / result.getStandardSeconds();
        //int bonus = (int) Math.max(0, Math.min(100, ratio * 100));
        int bonus = (int) Math.clamp(ratio * 100, 0, 100); // 이것은 java 버전 21 이상

        return base + bonus;
    }

    @Override
    public void onFail(Player player, MiniGame game) {
        // TODO
        player.loseLife();
        player.deductScore(50);
    }

    @Override
    public int maxScore() {
        return 300;
    }
}
