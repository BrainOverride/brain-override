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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
    public static void main(String[] args) {
        new NumberBaseball().play(new Player("테스트"), new NormalMode());
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

}
