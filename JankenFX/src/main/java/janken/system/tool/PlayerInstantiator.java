package janken.system.tool;

import janken.system.core.Player;
import janken.user.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * プレイヤークラスの管理およびインスタンス生成を行うクラス
 */
public class PlayerInstantiator {
    private final HashMap<String, Class<?>> playerClasses;

    public PlayerInstantiator() {
        playerClasses = new HashMap<>();
        loadBuiltInClasses();
    }

    /**
     * プログラム内蔵のPlayerクラスを読み込みます．
     */
    private void loadBuiltInClasses() {
        addPlayerClass("RandomPlayer(built-in)", RandomPlayer.class);
        addPlayerClass("RepeatPlayer(built-in)", RepeatPlayer.class);
        addPlayerClass("ComputerPlayer(built-in)", ComputerPlayer.class);
        addPlayerClass("PatternPredictPlayer(built-in)", PatternPredictPlayer.class);
        addPlayerClass("GUIHumanPlayer(built-in)", GUIHumanPlayer.class);
    }

    public String[] getPlayerClassKeys() {
        return playerClasses.keySet().toArray(new String[0]);
    }

    public Player instantiate(String key) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (Player)playerClasses.get(key).getDeclaredConstructor().newInstance();
    }

    public void addPlayerClass(String key, Class<?> clazz) {
        Class<Player> superClass = Player.class;
        if (superClass.isAssignableFrom(clazz)) {
            playerClasses.put(key, clazz);
        } else {
            System.out.println("Invalid Class.");
        }
    }

    public void removePlayerClass(String key) {
        Class<?> clazz = playerClasses.get(key);
        if (clazz != null) {
            playerClasses.remove(key);
        }
    }

    public boolean keyIsUsed(String key) {
        String[] keys = getPlayerClassKeys();
        for (String k : keys) {
            if (k.equals(key)) {
                return true;
            }
        }
        return false;
    }
}
