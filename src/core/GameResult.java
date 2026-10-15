package core;

// 담당: 심소민
// 한 판의 결과. 만든 뒤에는 바뀌지 않도록 모든 필드를 final로 둡니다.
public final class GameResult {
    public static final int BASE_SCORE = 100;

    private final boolean success;
    private final int baseScore;
    private final double elapsedSeconds;    // 소요 시간
    private final double standardSeconds;   // 시간 보너스 기준 시간

    public GameResult(boolean success, double elapsedSeconds, double standardSeconds) {
        this.success = success;
        this.baseScore = BASE_SCORE;
        this.elapsedSeconds = elapsedSeconds;
        this.standardSeconds = standardSeconds;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getBaseScore() {
        return baseScore;
    }

    public double getElapsedSeconds() {
        return elapsedSeconds;
    }

    public double getStandardSeconds() {
        return standardSeconds;
    }
}
