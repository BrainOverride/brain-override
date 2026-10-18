package games;

import core.*;

import java.util.*;

// 담당: 심소민
// 설명서: docs/games/stroop.md
public class StroopTest extends MiniGame {

    private static final int QUESTIONS = 5;
    private static final int GOAL = 4;
    private static final int MAX_SAME_INSTRUCTION = 2; // Hard: 같은 지시는 최대 2연속까지
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

    // Hard 전용: 문제마다 정답 기준이 바뀜
    private enum Instruction {
        SAY_COLOR("🎨 색을 말해라"),
        READ_WORD("📖 글자를 읽어라");

        final String text;

        Instruction(String text) {
            this.text = text;
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

    private static boolean isHard(Difficulty difficulty) {
        return difficulty.getLabel().equals("Hard");
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

    // 50% 확률로 지시 선택. 같은 지시가 이미 2번 연속이면 반대 지시로 강제
    // → 같은 지시가 계속 나오면 "전환" 자체가 사라지기 때문
    private static Instruction pickInstruction(Instruction last, int streak) {
        if (last != null && streak >= MAX_SAME_INSTRUCTION) {
            return last == Instruction.SAY_COLOR ? Instruction.READ_WORD : Instruction.SAY_COLOR;
        }
        return random.nextBoolean() ? Instruction.SAY_COLOR : Instruction.READ_WORD;
    }

    // ───────────────────────── 출력 ─────────────────────────

    private static void printGuide(Difficulty difficulty) {
        System.out.printf("=== 스트룹 테스트 (%s) ===%n", difficulty.getLabel());
        if (isHard(difficulty)) {
            System.out.println("문제마다 지시가 바뀝니다. 지시를 먼저 확인하세요!");
            System.out.println("  " + Instruction.SAY_COLOR.text + " → 글자의 '색'");
            System.out.println("  " + Instruction.READ_WORD.text + " → 글자의 '뜻'");
        } else {
            System.out.println("글자의 '뜻'이 아니라 '색'을 고르세요.");
        }
        printChoices();
        System.out.println();
    }

    private static void printChoices() {
        StringBuilder sb = new StringBuilder();
        for (StroopColor c : StroopColor.values()) {
            sb.append(c.number()).append(" ").append(c.label).append("  ");
        }
        System.out.println(sb.toString().trim());
    }

    // ───────────────────────── 게임 진행 ─────────────────────────

    @Override
    protected GameResult playRound(Difficulty difficulty) {
        double matchRatio = getMatchRatio(difficulty);
        boolean hard = isHard(difficulty);
        int correct = 0;

        Instruction last = null;
        int streak = 0;

        printGuide(difficulty);
        long startTime = System.currentTimeMillis();

        for (int q = 1; q <= QUESTIONS; q++) {
            StroopColor word = randomColor();
            StroopColor ink = pickInk(word, matchRatio);

            // Easy/Normal은 항상 "색을 말해라", Hard만 매 문제 지시를 뽑음
            Instruction instruction = Instruction.SAY_COLOR;
            if (hard) {
                instruction = pickInstruction(last, streak);
                streak = (instruction == last) ? streak + 1 : 1;
                last = instruction;
            }

            // 지시에 따라 정답과 함정(억눌러야 할 정보)이 서로 바뀜
            StroopColor answer = (instruction == Instruction.SAY_COLOR) ? ink : word;
            StroopColor trap = (instruction == Instruction.SAY_COLOR) ? word : ink;

            if (hard) {
                System.out.printf("[%d/%d] 지시: %s      %s%s%s%n",
                        q, QUESTIONS, instruction.text, ink.code, word.label, RESET);
            } else {
                System.out.printf("[%d/%d]   %s%s%s%n", q, QUESTIONS, ink.code, word.label, RESET);
            }

            // readInt가 1~4 범위 검증과 재입력을 처리 → 잘못된 입력은 문제 수를 차감하지 않음
            int input = ConsoleUtil.readInt("입력: ", 1, 4);

            if (input == answer.number()) {
                correct++;
                System.out.println("정답!");
            } else if (input == trap.number()) {
                // 함정을 고른 오답 = 억제에 실패한 것 → 무엇에 걸렸는지 알려줌
                String hint = (instruction == Instruction.SAY_COLOR) ? "글자를 읽었네요!" : "색을 봤네요!";
                System.out.printf("%s (정답: %d %s)%n", hint, answer.number(), answer.label);
            } else {
                System.out.printf("오답 (정답: %d %s)%n", answer.number(), answer.label);
            }
            System.out.println();
        }

        double elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0;
        boolean success = correct >= GOAL;
        System.out.printf("%d / %d 정답 → %s%n", correct, QUESTIONS, success ? "성공" : "실패");

        return new GameResult(success, elapsedSeconds, getStandardSeconds(difficulty));
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new StroopTest().play(new Player("테스트"), new NormalMode());
    }
}