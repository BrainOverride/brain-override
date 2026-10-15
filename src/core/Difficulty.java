package core;

// 담당: 정종일
// 난이도는 "숫자"가 아니라 점수 계산과 실패 처리 "동작"이 다릅니다.
public abstract class Difficulty {
    public abstract String getLabel();                         // "Easy" / "Normal" / "Hard"

    public abstract int calcScore(GameResult result);

    public abstract void onFail(Player player, MiniGame game);  // EasyMode는 여기서 game.playRound(this)로 재도전

    // 1판 최대 점수 (두뇌 나이의 만점 계산용)
    public abstract int maxScore();
}
