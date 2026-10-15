package app;

import core.MiniGame;
import core.Player;

import java.util.List;

// 담당: 나혜빈
public abstract class GameMode {
    public abstract String getLabel();                       // 메뉴에 보일 이름

    public abstract void run(Player player, List<MiniGame> games);
}
