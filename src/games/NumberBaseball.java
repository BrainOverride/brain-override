package games;

import core.Difficulty;
import core.GameResult;
import core.MiniGame;
import core.NormalMode;
import core.Player;

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
        // 난이도에 따른 숫자 자릿수와 최대 시도 횟수 설정
        int digits;
        int maxAttempts;

        // 선택한 난이도의 이름 가져오기
        String level = difficulty.getLabel();

        // 난이도별 조건문
        if (level.equals("Easy")) {
             digits = 3;
             maxAttempts = 10;
        } else if (level.equals("Normal")) {
            digits = 4;
            maxAttempts = 10;
        } else {
            digits = 4;
            maxAttempts = 8;
        }

        String answer = generateAnswer(digits);


        throw new UnsupportedOperationException("TODO");
    }

//    // 테스트용: 메뉴 없이 이 게임만 실행
//    public static void main(String[] args) {
//
//        new NumberBaseball().play(new Player("테스트"), new NormalMode());
//    }






    // 사용자 입력 검증 메서드
    private boolean isValidInput(String input, int digits) {
        // 입력한 숫자의 자릿수가 맞는지 확인
        if (input.length() != digits ) {
            return false;
        }
        // 숫자가 아닌 문자가 포함되어 있으면 유효하지 않은 입력으로 처리
        if (!(input.matches("[0-9]+"))) {
            return false;
        }
        // 입력한 숫자에 중복이 있는지 검사
        for (int i=0; i<input.length(); i++) {
            for (int j=i+1; j<input.length(); j++) {
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
        for (int i=0; i<10 ; i++) {
            numbers.add(i);
        }

        // 숫자 순서 랜덤 변경
        Collections.shuffle(numbers);

        // 정답을 이어 붙일 객체 생성
        StringBuilder answer = new StringBuilder();

        // 앞에서  digits개 선택
        for (int i=0 ; i<digits;i++){
            answer.append(numbers.get(i));
        }

        // 정답을 문자열로 반환
        return answer.toString();
    }

    // 정답과 입력값의 숫자 및 위치가 일치하는 개수 계산
    private int countStrikes(String answer, String input) {
        int strikes = 0;

        for (int i=0 ; i<input.length(); i++){
            if (answer.charAt(i) == input.charAt(i)) {
                strikes ++;
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

}
