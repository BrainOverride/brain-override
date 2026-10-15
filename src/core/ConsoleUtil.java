package core;

import java.util.Scanner;

// 담당: 심소민
// 콘솔 입출력 도우미. 모든 게임이 같은 입력 버그를 반복하지 않도록 한 곳에 모읍니다.
// Scanner는 프로그램 전체에서 이것 하나만 씁니다. 절대 close()하지 않습니다.
public final class ConsoleUtil {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleUtil() {
    }

    // 한 줄 입력. 모든 입력은 nextLine()으로 받습니다 (nextInt() 금지).
    public static String readLine(String prompt) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // 정수가 들어올 때까지 재입력 요청
    public static int readInt(String prompt) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // min~max 범위의 정수가 들어올 때까지 재입력 요청
    public static int readInt(String prompt, int min, int max) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // InterruptedException 처리를 여기서 한 번만 합니다.
    public static void sleep(long millis) {
        // TODO
    }

    // ANSI 화면 지우기. IntelliJ Run 콘솔에서는 빈 줄로 밀어내는 방식으로 대체
    public static void clearScreen() {
        // TODO
    }

    // text를 colorCode 색으로 칠하고 뒤에 RESET을 붙여 반환
    public static String color(String text, String colorCode) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
