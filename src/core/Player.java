package core;

import java.util.ArrayList;
import java.util.List;

// 담당: 정종일
// 캡슐화: 목숨, 점수, 기록은 private이고 메서드로만 바꿀 수 있습니다.
public class Player {
    public static final int MAX_LIVES = 3;

    private final String name;
    private int lives = MAX_LIVES;
    private int score = 0;
    private final List<String> history = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public void loseLife() {
        // TODO 0 미만으로 내려가지 않게
    }

    public void addScore(int amount) {
        // TODO
    }

    public void deductScore(int amount) {
        // TODO 0점 미만 불가
    }

    public void record(String gameName, GameResult result) {
        // TODO history에 한 줄 추가
    }

    public boolean isAlive() {
        return lives > 0;
    }

    // 원본을 지키기 위해 복사본을 반환
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }
}
