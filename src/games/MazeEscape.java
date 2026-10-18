package games;

import core.*;

import java.util.Random;

// 담당: 나혜빈
// 설명서: docs/games/maze_escape.md
public class MazeEscape extends MiniGame {

    private static final int MAX_ATTEMPTS = 3;
    private final Random random = new Random();

    public MazeEscape() {
        super("미로 탈출");
    }

    @Override
    protected GameResult playRound(Difficulty difficulty) {

        // 1. 난이도 설정
        String difficultyLabel = difficulty.getLabel();

        boolean isEasy = difficultyLabel.equalsIgnoreCase("Easy");
        boolean isHard = difficultyLabel.equalsIgnoreCase("Hard");

        double standardSeconds;

        if (isEasy) {
            standardSeconds = 60;
        } else if (isHard) {
            standardSeconds = 150;
        } else {
            standardSeconds = 120;
        }

        // 2. 난이도별 미로 중 하나를 랜덤 선택
        String[][] mazes = isEasy ? MazeData.EASY : MazeData.NORMAL;
        String[] maze = mazes[random.nextInt(mazes.length)];

        // 3. 출발점 S 찾기
        int startRow = -1;
        int startCol = -1;

        for (int row = 0; row < maze.length; row++) {
            int col = maze[row].indexOf('S');

            if (col != -1) {
                startRow = row;
                startCol = col;
                break;
            }
        }

        // 4. 게임 시작 시간
        long startTime = System.currentTimeMillis();

        // 5. 최대 3회 시도
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {

            System.out.println();
            System.out.println("=== 미로 탈출 (" + difficultyLabel
                    + ") === 시도 " + attempt + "/" + MAX_ATTEMPTS);

            if (isHard) {
                System.out.println("⚠ 좌우 반전! L = 오른쪽, R = 왼쪽");
            }

            printMaze(maze);

            // 6. 경로 입력 및 검증
            String input;

            while (true) {
                input = ConsoleUtil.readLine("경로 입력 (U/D/L/R): ")
                        .toUpperCase()
                        .replaceAll("\\s+", "");

                if (input.matches("[UDLR]+")) {
                    break;
                }

                System.out.println("U, D, L, R만 입력하세요.");
            }

            // 7. 매 시도마다 출발점에서 시작
            int row = startRow;
            int col = startCol;

            // 원본 미로는 변경하지 않음
            char[][] trace = copyMaze(maze);

            boolean hitWall = false;
            int hitStep = 0;

            // 8. 입력한 방향대로 한 칸씩 이동
            for (int i = 0; i < input.length(); i++) {

                char direction = input.charAt(i);

                int nextRow = row;
                int nextCol = col;

                switch (direction) {
                    case 'U':
                        nextRow--;
                        break;

                    case 'D':
                        nextRow++;
                        break;

                    case 'L':
                        nextCol += isHard ? 1 : -1;
                        break;

                    case 'R':
                        nextCol += isHard ? -1 : 1;
                        break;
                }

                // 9. 벽 충돌 및 범위 검사
                if (nextRow < 0 || nextRow >= maze.length
                        || nextCol < 0 || nextCol >= maze[nextRow].length()
                        || maze[nextRow].charAt(nextCol) == '#') {

                    hitWall = true;
                    hitStep = i + 1;

                    // 충돌 위치가 미로 안이면 X 표시
                    if (nextRow >= 0 && nextRow < trace.length
                            && nextCol >= 0 && nextCol < trace[nextRow].length) {
                        trace[nextRow][nextCol] = 'X';
                    }

                    break;
                }

                // 지나온 길 표시
                if (trace[row][col] == '.') {
                    trace[row][col] = '*';
                }

                row = nextRow;
                col = nextCol;

                if (trace[row][col] == '.') {
                    trace[row][col] = '*';
                }
            }

            // 10. 최종 위치가 출구인지 확인
            if (!hitWall && maze[row].charAt(col) == 'E') {

                double elapsedSeconds =
                        (System.currentTimeMillis() - startTime) / 1000.0;

                System.out.println("미로 탈출 성공!");
                System.out.println("소요 시간: " + elapsedSeconds + "초");

                return new GameResult(
                        true,
                        elapsedSeconds,
                        standardSeconds
                );
            }

            // 11. 실패 처리
            if (hitWall) {
                System.out.println(hitStep
                        + "번째 이동에서 벽에 부딪혔습니다!");
            } else {
                System.out.println("출구에 도착하지 못했습니다.");
            }

            printMaze(trace);

            if (attempt < MAX_ATTEMPTS) {
                System.out.println("다시 시도하세요!");
            }
        }

        // 12. 세 번 모두 실패
        double elapsedSeconds =
                (System.currentTimeMillis() - startTime) / 1000.0;

        System.out.println("미로 탈출 실패! 3번의 기회를 모두 사용했습니다.");

        return new GameResult(
                false,
                elapsedSeconds,
                standardSeconds
        );
    }

    // 원본 미로를 char[][]로 복사
    private char[][] copyMaze(String[] maze) {

        char[][] copy = new char[maze.length][];

        for (int i = 0; i < maze.length; i++) {
            copy[i] = maze[i].toCharArray();
        }

        return copy;
    }

    // 원본 미로 출력
    private void printMaze(String[] maze) {

        for (String line : maze) {
            for (char cell : line.toCharArray()) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    // 이동 경로가 표시된 미로 출력
    private void printMaze(char[][] maze) {

        for (char[] line : maze) {
            for (char cell : line) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    // 테스트용: 메뉴 없이 이 게임만 실행
//    public static void main(String[] args) {
//        new MazeEscape().play(new Player("테스트"), new HardMode());
//    }
}
