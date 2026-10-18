package app;

import core.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 담당: 나혜빈
// 6개 중 3개를 중복 없이 랜덤 순서로 연속 출제. 난이도는 시작할 때 한 번 고릅니다.
public class RandomChallengeMode extends GameMode {
    private static final int GAME_COUNT = 3;

    @Override
    public String getLabel() {
        return "랜덤 챌린지";
    }

    @Override
    public void run(Player player, List<MiniGame> games) {

        if (!player.isAlive()) {
            System.out.println("GAME OVER! 목숨이 모두 소진되었습니다.");
            return;
        }

        System.out.println("=== 랜덤 챌린지 ===");

        // 난이도 한 번만 선택
        System.out.println("1. Easy");
        System.out.println("2. Normal");
        System.out.println("3. Hard");

        int difficultyChoice = ConsoleUtil.readInt(
                "난이도를 선택하세요 (1~3): ", 1, 3
        );

        Difficulty difficulty;

        if (difficultyChoice == 1) {
            difficulty = new EasyMode();
        } else if (difficultyChoice == 2) {
            difficulty = new NormalMode();
        } else {
            difficulty = new HardMode();
        }

        // 원본 게임 목록 변경하지 않도록 복사
        List<MiniGame> shuffleGames = new ArrayList<>(games);

        // 게임 순서 무작위 변경
        Collections.shuffle(shuffleGames);

        // 중복 없이 3개 실행
        for (int i=0; i<GAME_COUNT && i<shuffleGames.size(); i++) {
            if (!player.isAlive()) {
                System.out.println("GAME OVER! 목숨이 모두 소진되었습니다.");
                return;
            }

            MiniGame game = shuffleGames.get(i);

            System.out.println();
            System.out.println("[" + (i + 1) + "/" + GAME_COUNT + "] "
                    + game.getName());

            game.play(player, difficulty);

            System.out.println("현재 점수: " + player.getScore());
            System.out.println("남은 목숨: " + player.getLives());

            if (!player.isAlive()) {
                System.out.println("GAME OVER! 목숨이 모두 소진되었습니다.");
                return;
            }
        }
        System.out.println("랜덤 챌린지가 종료되었습니다!");
    }
}
