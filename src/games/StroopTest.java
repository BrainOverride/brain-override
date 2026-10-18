package games;

import core.*;

import java.util.*;

// 담당: 심소민
// 설명서: docs/games/stroop.md
public class StroopTest extends MiniGame {

    private static final int QUESTIONS = 5;
    private static final int GOAL = 4;
    private static final String RESET = "\u001B[0m";
    private static final Random random = new Random();

    // 색 이름과 색 코드를 한 묶음으로 관리 → 이름과 코드가 따로 놀 수 없음
    // 선언 순서 = 입력 번호(1~4)
    private enum StroopColor {
        RED("빨강", "\u001B[91m"),
        GREEN("초록", "\u001B[92m"),
        YELLOW("노랑", "\u001B[93m"),
        BLUE("파랑", "\u001B[94m");

        final String label;
        final String code;

        StroopColor(String label, String code) {
            this.label = label;
            this.code = code;
        }

        int number() {
            return ordinal() + 1;
        }
    }

    public StroopTest() {
        super("스트룹 테스트");
    }

    // ───────────────────────── 난이도 설정 ─────────────────────────

    // 뜻과 색이 일치할 확률 (Easy=60%, Normal/Hard=0%)
    private static double getMatchRatio(Difficulty difficulty) {
        return difficulty.getLabel().equals("Easy") ? 0.6 : 0.0;
    }

    // 시간 보너스 기준 (Easy=30, Normal=25, Hard=30)
    private static double getStandardSeconds(Difficulty difficulty) {
        return difficulty.getLabel().equals("Normal") ? 25 : 30;
    }

    // ───────────────────────── 문제 생성 ─────────────────────────

    private static StroopColor randomColor() {
        StroopColor[] all = StroopColor.values();
        return all[random.nextInt(all.length)];
    }

    // 일치 비율에 따라 글자 색 결정. 불일치면 글자 뜻과 다른 색 중에서 고름
    private static StroopColor pickInk(StroopColor word, double matchRatio) {
        if (random.nextDouble() < matchRatio) {
            return word;
        }
        StroopColor ink;
        do {
            ink = randomColor();
        } while (ink == word);
        return ink;
    }

    // ───────────────────────── 게임 진행 ─────────────────────────

    @Override
    protected GameResult playRound(Difficulty difficulty) {
        double matchRatio = getMatchRatio(difficulty);

        // TODO: 입력/채점은 다음 커밋에서 구현 — 지금은 문제 생성과 색 출력만 확인
        for (int q = 1; q <= QUESTIONS; q++) {
            StroopColor word = randomColor();
            StroopColor ink = pickInk(word, matchRatio);
            System.out.printf("[%d/%d]   %s%s%s%n", q, QUESTIONS, ink.code, word.label, RESET);
        }

        return new GameResult(false, 0, getStandardSeconds(difficulty));
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new StroopTest().play(new Player("테스트"), new NormalMode());
    }
}