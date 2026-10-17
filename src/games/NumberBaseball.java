package games;

import core.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 담당: 나혜빈
// 설명서: docs/games/number_baseball.md
public class NumberBaseball extends MiniGame {

    public NumberBaseball() {
        super("숫자야구");
    }

    @Override
    protected GameResult playRound(Difficulty difficulty) {
        // 난이도별 숫자 자릿수, 최대 시도 횟수, 기준 시간 설정
        int digits;
        int maxAttempts;
        double standardSeconds;

        // 선택한 난이도의 이름 가져오기
        String level = difficulty.getLabel();

        // 난이도별 조건문
        if (level.equals("Easy")) {
            digits = 3;
            maxAttempts = 10;
            standardSeconds = 180;
        } else if (level.equals("Normal")) {
            digits = 4;
            maxAttempts = 10;
            standardSeconds = 180;
        } else {
            digits = 4;
            maxAttempts = 8;
            standardSeconds = 150;
        }

        // 난이도에 맞는 자릿수의 랜덤 정답 생성
        String answer = generateAnswer(digits);

        // Hard 난이도 시작 시 거짓 힌트 규칙 안내
        if (level.equals("Hard")) {
            System.out.println("힌트 중 1번은 거짓입니다.");
        }

        // 게임 시작 시간 기록
        long startTime = System.nanoTime();

        // 현재 시도 횟수
        int attempt = 0;

        // Hard 난이도에서 거짓 힌트를 제공할 시도 번호
        int lieAttempt = level.equals("Hard")
                ? (int) (Math.random() * (maxAttempts - 1)) + 1
                : -1;

        // 실제로 거짓 힌트를 제공했는지 기록
        int usedLieAttempt = -1;


        // 최대 시도 횟수에 도달할 때까지 반복
        while (attempt < maxAttempts) {
            // 사용자에게 숫자 입력받기
            String input = ConsoleUtil.readLine("숫자를 입력하세요: ");

            // 잘못된 입력이면 다시 입력받기
            if (!isValidInput(input, digits)) {
                System.out.println("숫자를 다시 입력하세요:");
                continue;
            }

            // 유효한 입력만 시도 횟수에 포함
            attempt++;

            // 정답과 입력값 비교하여 스트라이크와 볼 계산
            int strikes = countStrikes(answer, input);
            int balls = countBalls(answer, input);

            // 정답을 맞히지 못했고 거짓 힌트 차례라면 힌트를 변경
            if (level.equals("Hard") && attempt == lieAttempt && strikes != digits) {
                int[] fakeHint = generateFakeHint(strikes, balls, digits);

                System.out.println(fakeHint[0] + "S   " + fakeHint[1] + "B");
                usedLieAttempt = attempt;
            } else {
                System.out.println(strikes + "S" + "   " + balls + "B");
            }

            // 모든 자릿수 일치하면 성공처리
            if (strikes == digits) {
                System.out.println("정답입니다!");

            if (level.equals("Hard")) {
                printLieResult(usedLieAttempt);
            }

                // 경과 시간 계산 후 성공 결과 반환
                long endTime = System.nanoTime();
                double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

                return new GameResult(true, elapsedSeconds, standardSeconds);
            }
        }

        // 최대 시도 횟수를 모두 사용한 경우
        System.out.println("실패했습니다! 정답은 " + answer + "입니다");

        if (level.equals("Hard")) {
            printLieResult(usedLieAttempt);
        }

        // 게임 종료 시간 및 경과 시간 계산
        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

        return new GameResult(false, elapsedSeconds, standardSeconds);
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {

        new NumberBaseball().play(new Player("테스트"), new HardMode());
    }


    // 사용자 입력 검증 메서드
    private boolean isValidInput(String input, int digits) {
        // 입력한 숫자의 자릿수가 맞는지 확인
        if (input.length() != digits) {
            return false;
        }
        // 숫자가 아닌 문자가 포함되어 있으면 유효하지 않은 입력으로 처리
        if (!(input.matches("[0-9]+"))) {
            return false;
        }
        // 입력한 숫자에 중복이 있는지 검사
        for (int i = 0; i < input.length(); i++) {
            for (int j = i + 1; j < input.length(); j++) {
                if (input.charAt(i) == input.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }


    // 중복 없는 랜덤 숫자를 생성하는 메서드
    private String generateAnswer(int digits) {
        // 숫자를 저장할 리스트 생성
        List<Integer> numbers = new ArrayList<>();

        // 0~9 저장
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }

        // 숫자 순서 랜덤 변경
        Collections.shuffle(numbers);

        // 정답을 이어 붙일 객체 생성
        StringBuilder answer = new StringBuilder();

        // 앞에서  digits개 선택
        for (int i = 0; i < digits; i++) {
            answer.append(numbers.get(i));
        }

        // 정답을 문자열로 반환
        return answer.toString();
    }

    // 정답과 입력값의 숫자 및 위치가 일치하는 개수 계산
    private int countStrikes(String answer, String input) {
        int strikes = 0;

        for (int i = 0; i < input.length(); i++) {
            if (answer.charAt(i) == input.charAt(i)) {
                strikes++;
            }
        }
        return strikes;
    }

    // 정답에 포함되어 있지만 위치가 다른 숫자의 개수 계산
    private int countBalls(String answer, String input) {
        int balls = 0;

        for (int i = 0; i < input.length(); i++) {
            if ((answer.indexOf(input.charAt(i)) >= 0)
                    && (answer.charAt(i) != input.charAt(i))) {
                balls++;
            }
        }
        return balls;
    }

    // 실제 힌트와 다르면서 숫자야구 규칙상 가능한 거짓 힌트 생성
    private int[] generateFakeHint(int strikes, int balls, int digits) {
        List<int[]> candidates = new ArrayList<>();

        for (int fakeStrikes = 0; fakeStrikes <= digits; fakeStrikes++) {
            for (int fakeBalls = 0; fakeBalls <= digits; fakeBalls++) {

                // 스트라이크와 볼의 합은 자릿수를 초과할 수 없음
                if (fakeStrikes + fakeBalls > digits) {
                    continue;
                }

                // 전체 자릿수 중 한 자리만 볼인 조합은 불가능
                if (fakeStrikes == digits - 1 && fakeBalls == 1) {
                    continue;
                }

                // 전체 스트라이크는 정답이므로 거짓 힌트에서 제외
                if (fakeStrikes == digits) {
                    continue;
                }

                // 실제 힌트와 같은 조합은 제외
                if (fakeStrikes == strikes && fakeBalls == balls) {
                    continue;
                }

                candidates.add(new int[]{fakeStrikes, fakeBalls});
            }
        }

        // 가능한 거짓 힌트 중 하나를 무작위 선택
        Collections.shuffle(candidates);
        return candidates.get(0);
    }

    // Hard 난이도 종료 시 거짓 힌트를 제공한 시도 공개
    private void printLieResult(int usedLieAttempt) {
        if (usedLieAttempt == -1) {
            System.out.println("거짓 힌트가 나오기 전에 게임이 종료되었습니다.");
        } else {
            System.out.println("거짓 힌트는 " + usedLieAttempt + "번째 시도에서 제공되었습니다!");
        }
    }

}
