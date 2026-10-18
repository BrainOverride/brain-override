package games.tictactoe;

// 담당: 정종일
// 공유할 코드 없이 "약속"만 있으므로 추상 클래스가 아니라 인터페이스입니다.
public interface AIStrategy {

    /*
    * AI가 지켜야 할 약속
    * 9칸을 담은 배열
    * ai.choose 반환(구현은 AI 쪽에서)
    * ai.choose 호출*/

    int choose(char[] board);    // 둘 칸의 인덱스(0~8)를 반환
}
