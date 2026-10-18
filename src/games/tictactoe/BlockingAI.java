package games.tictactoe;

import java.util.List;
import java.util.Random;

// 담당: 정종일
class BlockingAI implements AIStrategy {

    private final Random random = new Random();

    @Override
    public int choose(char[] board) {
        // TODO
//        throw new UnsupportedOperationException("TODO");

        /*
        * 빈칸 리스트 얻어서
        * 플레이어가 여기에 두면 이라고 가정한 판을 만들고
        * 그 판에서 플레이어가 이기면 그 칸이 막아야 할 칸이므로 즉시 반환
        * 막을 곳 없으면 무작위 칸
        * */

        List<Integer> empty = BoardUtil.emptyCells(board);

        for (int cell : empty) {
            char[] next = BoardUtil.place(board, cell, 'O');
            if (BoardUtil.winner(next) == 'O') {
                return cell;
            }
        }


        return empty.get(random.nextInt(empty.size()));

    }
}
