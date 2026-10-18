package core;

// 담당: 정종일
// 난이도는 "숫자"가 아니라 점수 계산과 실패 처리 "동작"이 다릅니다.
public abstract class Difficulty {
    public abstract String getLabel();                         // "Easy" / "Normal" / "Hard"

    public abstract int calcScore(GameResult result);

    // 실패 처리 후 "최종 결과"를 반환합니다.
    // Easy는 재도전한 결과를, Normal/Hard는 패널티를 적용한 뒤 원래 실패 결과를 그대로 돌려줍니다.
    // → 호출하는 MiniGame은 어떤 난이도인지 몰라도 최종 결과를 기록할 수 있습니다.
    public abstract GameResult onFail(Player player, MiniGame game, GameResult failed);

    // 1판 최대 점수 (두뇌 나이의 만점 계산용)
    public abstract int maxScore();

}
