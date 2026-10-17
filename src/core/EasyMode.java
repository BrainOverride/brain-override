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

    @Override
    public int maxScore() {
        return 100;
    }
}
