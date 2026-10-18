package core;

// 담당: 정종일
// 성공 100점 / 실패 시 재도전 1회, 재도전도 실패하면 목숨 -1
public class EasyMode extends Difficulty {
    @Override
    public String getLabel() {
        return "Easy";
    }

    @Override
    public int calcScore(GameResult result) {
        // TODO
        //throw new UnsupportedOperationException("TODO");
        return result.isSuccess() ? result.getBaseScore() : 0;
    }

    @Override
    public void onFail(Player player, MiniGame game) {
        // TODO
        GameResult retry = game.playRound(this);
        if (retry.isSuccess()) {
            player.addScore(calcScore(retry));
        } else {
            player.loseLife();
        }
    }

    public GameResult retry(MiniGame game) {
        System.out.println("\n실패했습니다! Easy 난이도는 재도전 기회가 1번 주어집니다.");
        System.out.println("=== 재도전 시작! ===");

        return game.playRound(this);
    }

    @Override
    public int maxScore() {
        return 100;
    }
}
