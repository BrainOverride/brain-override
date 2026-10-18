package games.tictactoe;


// 담당: 정종일
class MinimaxAI implements AIStrategy {

    /*
     * minimax : 가장 좋은 수를 고름
     * 점수 : AI(X) 승리 +1 / 무승부 0 / 플레이어(O) 승리 -1
     * AI는 점수 최대화, 플레이어는 최소화
     */

    private static final char AI = 'X';
    private static final char PLAYER = 'O';

    @Override
    public int choose(char[] board) {
        // TODO
//        throw new UnsupportedOperationException("TODO");

        /*
         * 빈 칸마다 AI가 두어 보고, 다음은 플레이어 차례(false)로 minimax 호출
         * 점수가 최고점보다 크면 그 칸 기억
         * bestCell = -1 : 아직 못 정함 (유효한 칸은 0~8)
         * MIN_VALUE : 어떤 점수보다 작은 초기값
         */

        int bestCell = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int cell : BoardUtil.emptyCells(board)) {
            int score = minimax(BoardUtil.place(board, cell, AI), false);
            if (score > bestScore) {
                bestScore = score;
                bestCell = cell;
            }
        }
        return bestCell;
    }

    private int minimax(char[] board, boolean aiTurn) {

        /*
         * 재귀 : 한 수씩 둔 판을 넘기며 끝날 때까지 자기 자신을 호출
         * 종료 조건 : 승부가 났거나 판이 가득 차면 점수 반환 (승자 검사가 먼저, 가득 참이 나중)
         * aiTurn : true면 AI 차례 -> 최댓값(Math.max), false면 플레이어 차례 -> 최솟값(Math.min)
         */

        char w = BoardUtil.winner(board);
        if (w == AI) {
            return 1;
        }
        if (w == PLAYER) {
            return -1;
        }
        if (BoardUtil.isFull(board)) {
            return 0;
        }
        if (aiTurn) {
            int best = Integer.MIN_VALUE;
            for (int cell : BoardUtil.emptyCells(board)) {
                best = Math.max(best, minimax(BoardUtil.place(board, cell, AI), false));
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int cell : BoardUtil.emptyCells(board)) {
                best = Math.min(best, minimax(BoardUtil.place(board, cell, PLAYER), true));
            }
            return best;
        }
    }

}
