package games;

import core.*;

import java.util.*;

// 담당: 심소민
// 설명서: docs/games/card_match.md
public class CardMatch extends MiniGame {

    private static final int COLUMNS = 4;            // 보드 열 수
    private static final int WRONG_DELAY_MILLIS = 1500;
    private static final int HARD_SHUFFLE_STREAK = 3; // Hard: 연속 실패 시 섞기 기준

    public CardMatch() {
        super("카드 뒤집기");
    }

    // ───────────────────────── 보드 출력 ─────────────────────────

    // showAll=true → 정답 전체 공개, reveal → 이번 턴에 뒤집은 칸만 공개
    private static void printBoard(List<Character> cards, boolean[] matched,
                                   int col, boolean showAll, int... reveal) {
        Set<Integer> open = new HashSet<>();
        for (int r : reveal) open.add(r);

        for (int i = 0; i < cards.size(); i++) {
            boolean visible = showAll || matched[i] || open.contains(i);
            String face = visible ? String.valueOf(cards.get(i)) : "■";
            System.out.printf("[%2d] %s   ", i + 1, face);
            if ((i + 1) % col == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    // ───────────────────────── 보드 생성 ─────────────────────────

    // 서로 다른 문자 num개 선택 (알파벳을 섞어서 앞에서부터 잘라 씀 → 중복 불가)
    private static List<Character> randomChoice(int num) {
        List<Character> alphabet = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            alphabet.add(c);
        }
        Collections.shuffle(alphabet);
        return new ArrayList<>(alphabet.subList(0, num));
    }

    // 난이도별 카드 목록 생성 (Easy=4쌍, Normal/Hard=8쌍)
    public static List<Character> createCardList(Difficulty difficulty) {
        int pairCount = difficulty.getLabel().equals("Easy") ? 4 : 8;

        List<Character> cards = new ArrayList<>();
        for (Character letter : randomChoice(pairCount)) {
            cards.add(letter);
            cards.add(letter);
        }
        Collections.shuffle(cards);
        return cards;
    }

    // ───────────────────────── 규칙 ─────────────────────────

    // 두 인덱스의 카드가 같은 그림인지 판정
    private static boolean isMatch(List<Character> cards, int a, int b) {
        return cards.get(a).equals(cards.get(b));
    }

    // 모든 쌍을 찾았는지
    private static boolean allMatched(boolean[] matched) {
        for (boolean m : matched) {
            if (!m) return false;
        }
        return true;
    }

    // 난이도별 시도 횟수 (Easy=12, Normal/Hard=20)
    private static int getMaxTries(Difficulty difficulty) {
        return difficulty.getLabel().equals("Easy") ? 12 : 20;
    }

    // 난이도별 암기 시간 (Easy=15초, Normal/Hard=20초)
    // Hard는 판 크기가 Normal과 같고 "섞기 규칙"만 다르므로 암기 시간도 Normal과 같게 둠
    private static int getMemorizeMillis(Difficulty difficulty) {
        return difficulty.getLabel().equals("Easy") ? 15000 : 20000;
    }

    // 난이도별 시간 보너스 기준 (Easy=90, Normal/Hard=180)
    private static double getStandardSeconds(Difficulty difficulty) {
        return difficulty.getLabel().equals("Easy") ? 90 : 180;
    }

    // Hard 전용 — 못 찾은 카드끼리만 위치 섞기
    // 못 찾은 칸의 문자들끼리만 자리를 바꾸므로 짝(2장씩)이 항상 유지되고, 맞춘 카드는 고정됨
    private static void shuffleRemaining(List<Character> cards, boolean[] matched) {
        List<Integer> idx = new ArrayList<>();
        List<Character> remain = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (!matched[i]) {
                idx.add(i);
                remain.add(cards.get(i));
            }
        }
        Collections.shuffle(remain);
        for (int k = 0; k < idx.size(); k++) {
            cards.set(idx.get(k), remain.get(k));
        }
    }

    // ───────────────────────── 입력 ─────────────────────────

    // 올바른 입력이 들어올 때까지 반복 → 잘못된 입력은 시도 횟수를 차감하지 않음
    private static int[] readPair(List<Character> cards, boolean[] matched) {
        while (true) {
            String line = ConsoleUtil.readLine("카드 번호 2개를 입력하세요 (예: 3 7): ");
            String[] parts = line.trim().split("\\s+");

            if (parts.length != 2) {
                System.out.println("숫자 2개를 공백으로 구분해 입력하세요.");
                continue;
            }
            try {
                int a = Integer.parseInt(parts[0]) - 1; // 화면은 1부터, 인덱스는 0부터
                int b = Integer.parseInt(parts[1]) - 1;

                // 범위 검사를 먼저 해야 matched[a] 접근 시 예외가 나지 않음
                if (a < 0 || b < 0 || a >= cards.size() || b >= cards.size()) {
                    System.out.printf("1 ~ %d 사이의 번호를 입력하세요.%n", cards.size());
                    continue;
                }
                if (a == b) {
                    System.out.println("서로 다른 카드 2장을 고르세요.");
                    continue;
                }
                if (matched[a] || matched[b]) {
                    System.out.println("이미 찾은 카드입니다.");
                    continue;
                }
                return new int[]{a, b};
            } catch (NumberFormatException e) {
                System.out.println("숫자만 입력하세요.");
            }
        }
    }

    // ───────────────────────── 유틸 ─────────────────────────
    // ConsoleUtil에 sleep / clearScreen이 있다면 그쪽으로 교체해도 됨

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태 복원
        }
    }

    // IDE 콘솔에서도 동작하도록 빈 줄로 밀어내는 방식 사용
    private static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // ───────────────────────── 게임 진행 ─────────────────────────

    @Override
    protected GameResult playRound(Difficulty difficulty) {
        List<Character> cards = createCardList(difficulty);
        boolean[] matched = new boolean[cards.size()];
        int maxTries = getMaxTries(difficulty);
        boolean isHard = difficulty.getLabel().equals("Hard");
        int failStreak = 0;
        boolean success = false;

        // 1. 암기 시간: 정답 공개 → 화면 지우기
        int memorizeMillis = getMemorizeMillis(difficulty);
        System.out.printf("%d초 동안 카드 위치를 외우세요!%n", memorizeMillis / 1000);
        printBoard(cards, matched, COLUMNS, true);
        sleep(memorizeMillis);
        clearScreen();

        // 타이머는 암기 시간 이후부터 측정
        long startTime = System.currentTimeMillis();

        for (int tries = 1; tries <= maxTries; tries++) {
            System.out.printf("남은 시도: %d / %d%n", maxTries - tries + 1, maxTries);
            printBoard(cards, matched, COLUMNS, false);

            // 2. 카드 2장 선택
            int[] pick = readPair(cards, matched);
            int a = pick[0];
            int b = pick[1];

            // 3. 선택한 2장 공개
            printBoard(cards, matched, COLUMNS, false, a, b);

            // 4. 판정
            if (isMatch(cards, a, b)) {
                matched[a] = true;
                matched[b] = true;
                failStreak = 0;
                System.out.println("짝을 찾았습니다!");
                sleep(800);
            } else {
                System.out.println("틀렸습니다.");
                failStreak++;
                sleep(WRONG_DELAY_MILLIS);

                if (isHard && failStreak == HARD_SHUFFLE_STREAK) {
                    shuffleRemaining(cards, matched);
                    failStreak = 0;
                    System.out.println("연속 " + HARD_SHUFFLE_STREAK + "회 실패! 남은 카드가 섞였습니다.");
                    sleep(WRONG_DELAY_MILLIS);
                }
            }
            clearScreen(); // 공개된 카드가 화면에 남지 않도록

            // 5. 모든 쌍을 찾았으면 성공
            if (allMatched(matched)) {
                success = true;
                break;
            }
        }

        // 결과 안내
        printBoard(cards, matched, COLUMNS, true);
        System.out.println(success ? "모든 짝을 찾았습니다!" : "시도 횟수를 모두 사용했습니다.");

        double elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0;
        return new GameResult(success, elapsedSeconds, getStandardSeconds(difficulty));
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new CardMatch().play(new Player("테스트"), new EasyMode());
    }
}