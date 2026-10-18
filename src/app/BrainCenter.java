package app;

import core.ConsoleUtil;
import core.MiniGame;
import core.Player;
import games.GameRegistry;

import java.util.List;
import java.util.Map;

// 담당: 나혜빈
// 메인 메뉴. 구체 게임 이름을 모르고 List<MiniGame>만 다룹니다.
public class BrainCenter {
    private final List<MiniGame> games = GameRegistry.all();
    private final Map<Integer, GameMode> modes = Map.of(
        1, new FreeMode(),
        2, new RandomChallengeMode()
    );

    public void start() {
        // TODO 이름 입력 → 모드 선택 → modes.get(choice).run(player, games) → 결과 화면(총점, 두뇌 나이)

        // 1. 시작 화면
        System.out.println("================================");
        System.out.println("         BRAIN OVERRIDE");
        System.out.println("================================");

        // 2. 플레이어 이름 입력
        String name;

        while (true) {
            name = ConsoleUtil.readLine("이름을 입력하세요: ").trim();

            if (!name.isEmpty()) {
                break;
            }

            System.out.println("이름을 입력해주세요.");
        }

        // 플레이어 생성 (목숨 3개, 점수 0점)
        Player player = new Player(name);

        // 3. 메인 메뉴 반복
        while (player.isAlive()) {

            System.out.println();
            System.out.println("=== 메인 메뉴 ===");
            System.out.println("플레이어: " + player.getName());
            System.out.println("현재 점수: " + player.getScore());
            System.out.println("남은 목숨: " + player.getLives());

            System.out.println("1. " + modes.get(1).getLabel());
            System.out.println("2. " + modes.get(2).getLabel());
            System.out.println("0. 종료");

            int choice = ConsoleUtil.readInt(
                    "메뉴를 선택하세요 (0~2): ", 0, 2
            );

            // 종료 선택
            if (choice == 0) {
                break;
            }

            // 4. 선택한 게임 모드 실행
            modes.get(choice).run(player, games);

            // 랜덤 챌린지 완료 후 결과 화면으로 이동
            if (choice == 2) {
                break;
            }

            // 자유 모드는 한 판 종료 후 메인 메뉴로 복귀
        }

        // 5. 게임 오버 확인
        if (!player.isAlive()) {
            System.out.println();
            System.out.println("=== GAME OVER ===");
            System.out.println("목숨이 모두 소진되었습니다.");
        }

        // 6. 최종 결과 화면
        System.out.println();
        System.out.println("=== 최종 결과 ===");

        // 1. 게임별 결과 출력
        if (player.getHistory().isEmpty()) {
            System.out.println("플레이 기록이 없습니다.");
        } else {
            for (String record : player.getHistory()) {
                System.out.println(record);
            }
        }

        // 2. 총점과 만점 가져오기
        int score = player.getScore();
        int maxScore = player.getMaxScore();

        System.out.println("총점: " + score + " / 만점: " + maxScore);

        // 3. 두뇌 나이 계산
        if (maxScore > 0) {
            double brainAge = 60 - 40.0 * score / maxScore;

            // 두뇌 나이를 20~60세로 제한
            brainAge = Math.max(20, Math.min(60, brainAge));

            System.out.printf("두뇌 나이: %.1f세%n", brainAge);
        } else {
            System.out.println("두뇌 나이: 계산 불가 (플레이 기록 없음)");
        }

        // 4. 남은 목숨 출력
        System.out.println("남은 목숨: " + player.getLives());
    }
}
