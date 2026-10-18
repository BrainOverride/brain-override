package games;

import core.*;

import java.util.Random;

/*
 * MiniGame과 다형성
 * 공통 실행 흐름
 * play() final이여서 게임별 자식 클래스가 공통 점수 처리 흐름을 덮어쓰지 못하고,
 * 게임마다 달라야되는 playRound()만 구현
 * MentalMath의 playRound() 호출.
 * */
// 담당: 정종일
// 설명서: docs/games/mental_math.md
public class MentalMath extends MiniGame {

    /*
     * 게임 제한시간 30초, 성공 필요 정답 수 5개 상수로
     * */
    private static final int TIME_LIMIT_SEC = 30;
    private static final int GOAL = 5;
    private final Random random = new Random();


    public MentalMath() {
        super("암산 스피드");
    }

    /*
     * 난이도 판별, 준비
     * getLabel 난이도 표시
     * easy, hard 이후 문제 생성, Hard 규칙
     * normal은 기본 규칙으로
     * readLine 엔터 누르면 그때부터 startTime
     * correctCount : 정답수, questionNumber : 문제 번호, fifthCorrectAt : 5번째 정답 시점
     * fifthCorrectAt는 나중에 정답 시간 계산 위해 double 타입으로
     * */
    @Override
    protected GameResult playRound(Difficulty difficulty) {
        // TODO
//       throw new UnsupportedOperationException("TODO");

        String level = difficulty.getLabel();
        boolean easy = "Easy".equalsIgnoreCase(level);
        boolean hard = "Hard".equalsIgnoreCase(level);

        System.out.println("30초 동안 " + GOAL + "문제 이상 맞히세요.");
        ConsoleUtil.readLine("enter를 누르면 시작합니다.");

        long startTime = System.currentTimeMillis();
        int correctCount = 0;
        int questionNumber = 1;
        double fifthCorrectAt = TIME_LIMIT_SEC;

        /*
         * har면 3문제마다 연산 반전
         * hard인가, 3문제마다 구간 true, false 반환 밑에 if문 실행
         * hard인가, 4,7,10번째 규칙이 전환되는 문제면 안내 문구 출력
         * 1번째 문제 제외, 처음부터 정상 규칙 안내 문자 x
         * */
        while (elapsedSeconds(startTime) < TIME_LIMIT_SEC) {
            boolean reversed = hard && (((questionNumber - 1) / 3) % 2 == 1);

            if (hard && questionNumber > 1 && (questionNumber - 1) % 3 == 0) {
                if (reversed) {
                    System.out.println("지금부터 +는 빼기, -는 더하기입니다.");
                } else {
                    System.out.println("이제 +는 더하기, -는 빼기입니다.");
                }
            }

            /*
             * 문제 연산을 저장할 Problem 객체를 받음
             * %d %s %d : 정수, 문자열 자리
             * Math.max : 0 밑으로 안 떨어지게
             * Math.ceil : 남은 시간 올림
             * readLine 한 줄 받을 때까지 기다림
             * */
            Problem problem = createProblem(easy, reversed);
            boolean answered = false;

            while (!answered) {
                double elapsed = elapsedSeconds(startTime);
                if (elapsed >= TIME_LIMIT_SEC) {
                    break;
                }

                int remaining = Math.max(0, (int) Math.ceil(TIME_LIMIT_SEC - elapsed));
                String prompt = String.format(
                        "[남은 %d초] %d %s %d = ?",
                        remaining, problem.left, problem.operatorText, problem.right
                );
                String line = ConsoleUtil.readLine(prompt);

                /*
                 * 입력 받은 직후 경과 시간 다시 확인, 제한 시간 30초 지나면 입력한 답 채점 안 함
                 * 숫자가 아니라 다른 거 치면 똑같은 문제 다시
                 * */

                elapsed = elapsedSeconds(startTime);
                if (elapsed >= TIME_LIMIT_SEC) {
                    break;
                }

                int answer;
                try {
                    answer = Integer.parseInt(line.trim());
                } catch (NumberFormatException e) {
                    System.out.println("숫자를 입력하세요.");
                    continue;
                }

                /* 숫자를 제대로 치면 정답과 비교, 보너스 시간은 5문제 맞추는 시점*/
                answered = true;
                if (answer == problem.answer) {
                    correctCount++;
                    System.out.println("정답입니다!");
                    if (correctCount == GOAL) {
                        fifthCorrectAt = elapsed;
                    }
                } else {
                    System.out.println("오답입니다. 정답: " + problem.answer);
                }
            }

            /* 현재 문제 잘 처리했으면 다음 문제로 이동*/
            if (!answered) {
                break;
            }
            questionNumber++;
        }

        boolean success = correctCount >= GOAL;
        System.out.println("시간 종료 " + correctCount + "문제 정답 "
                + (success ? "성공" : "실패"));

        double elapsedForResult = success ? fifthCorrectAt : TIME_LIMIT_SEC;

        return new GameResult(success, elapsedForResult, TIME_LIMIT_SEC);
    }


    /* 1. 연산자 선택(easy인지 아닌지)
     *  2. 숫자 생성(숫자 범위 무작위)
     *  3. 실제 연산 확인(반전 -> 덧셈? 뺄셈?)
     *  4. 뺄셈 숫자 순서 정리 및 정답 계산(음수 방지 실제 정답 산출)
     *  5. Problem으로 객체 반환
     * */
    /*
     * 문제 만드는 곳
     * easy모드인지, 연산자 반전 상태인지 전달을 받음
     * easy모드인지 삼항연산자 이용해서 +,- 만 나오게 다른 것은 * 까지 포함
     * operators의 length를 가져와 +,-,*를 랜덤으로 가져옴.
     * (+, -), (*) 마다 나오는 숫자 범위 다르게, randomBetween(min, max)
     * easy 모드면 최대 숫자가 20 으로
     * + 면 그대로. - 이면 음수가 나오지 않도록 왼쪽보다 오른쪽이 더 크면,
     * temp라는 임시 변수를 생성해서 오른쪽이랑 왼쪽 값 변경
     * */
    private Problem createProblem(boolean easy, boolean reversed) {
        char[] operators = easy
                ? new char[]{'+', '-'}
                : new char[]{'+', '-', '*'};
        char operator = operators[random.nextInt(operators.length)];

        int left;
        int right;

        if (operator == '*') {
            left = randomBetween(2, 9);
            right = randomBetween(2, 12);
        } else {
            int max = easy ? 20 : 50;
            left = randomBetween(1, max);
            right = randomBetween(1, max);

            boolean subtraction = (operator == '-' && !reversed)
                    || (operator == '+' && reversed);
            if (subtraction && left < right) {
                int temp = left;
                left = right;
                right = temp;
            }
        }

        /*
         * 여기가 반전 * 는 그대로 ㄱ
         * operatorText 는 화면에 표시할 기호
         * 완성하면 문제 객체 반환
         * */
        int answer;
        if (operator == '+') {
            answer = reversed ? left - right : left + right;
        } else if (operator == '-') {
            answer = reversed ? left + right : left - right;
        } else {
            answer = left * right;
        }

        String operatorText = operator == '*' ? "×" : String.valueOf(operator);

        return new Problem(left, right, operatorText, answer);
    }

    /*
     * 최소, 최대 값 을 모두 포함한 정수
     * */
    private int randomBetween(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /* 시작 이후 지난 시간 초단위로 반환 */
    private double elapsedSeconds(long startTime) {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }

    /*
     * 문제 하나 나타내는 정의
     * 문제랑 정답
     * 여기는 다른 파일에 정의를 해야 할 지 ?
     * */
    private static class Problem {
        private final int left;
        private final int right;
        private final String operatorText;
        private final int answer;

        private Problem(int left, int right, String operatorText, int answer) {
            this.left = left;
            this.right = right;
            this.operatorText = operatorText;
            this.answer = answer;
        }
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new MentalMath().play(new Player("테스트"), new NormalMode());
    }
}
