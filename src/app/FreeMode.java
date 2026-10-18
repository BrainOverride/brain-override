package app;

import core.*;

import java.util.List;

// 담당: 나혜빈
// 게임 1개와 난이도를 직접 고르고, 끝나면 메뉴로 돌아옵니다.
public class FreeMode extends GameMode {
    @Override
    public String getLabel() {
        return "자유 모드";
    }

    @Override
    public void run(Player player, List<MiniGame> games) {
        if (!player.isAlive()) {
            System.out.println("GAME OVER! 목숨이 모두 소진되었습니다.");
            return;
        }
        System.out.println("=== 자유 모드 ===");

        // 게임 목록 출력
        for (int i=0; i<games.size();i++) {
            System.out.println((i+1)+". "+games.get(i).getName());
        }

        // 게임 1개 선택
        int gameChoice = ConsoleUtil.readInt(
                "게임을 선택하세요 (1~" + games.size() + "): ", 1, games.size()
        );

        MiniGame selectedGame = games.get(gameChoice -1);

        // 난이도 선택
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

        // 선택한 게임의 만점 누적
        player.addMaxScore(difficulty.maxScore());

        // 선택한 게임 실행
        selectedGame.play(player, difficulty);

        // 실행 후 결과 출력
        System.out.println("현재 점수: " + player.getScore());
        System.out.println("남은 목숨: " + player.getLives());

        // 전체 게임 종료 여부
        if (!player.isAlive()) {
            System.out.println("GAME OVER! 목숨이 모두 소진되었습니다.");
        }

        // run() 종료 -> BrainCenter 메뉴로 복귀

    }
}
