package games.tictactoe;

import java.util.List;
import java.util.Random;

// 담당: 정종일
class RandomAI implements AIStrategy {

    private final Random random = new Random();

    @Override
    public int choose(char[] board) {
        // TODO
//        throw new UnsupportedOperationException("TODO");

        /*
        * 빈 칸 번호 리스트 얻음
        * 번호 중 무작위로 뽑고 반환
        * */

        List<Integer> empty = BoardUtil.emptyCells(board);

        return empty.get(random.nextInt(empty.size()));

    }
}
