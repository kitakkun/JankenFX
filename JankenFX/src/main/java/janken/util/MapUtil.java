package janken.util;

import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;

import java.util.Map;

/**
 * Mapから欲しい情報を取得するための便利機能をまとめたクラス
 */
public class MapUtil {
    /**
     * 最も値が大きいキーを取得します
     * @param map 対象となるMapインスタンス
     * @return 最も値が大きいキー
     */
    public static Hand getMaxKey(Map<Hand, Integer> map) {
        Hand maxKey = null;
        Integer maxValue = 0;
        for (Map.Entry<Hand, Integer> entry : map.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }
        return maxKey;
    }

    public static int getValuesSum(Map<Judge, Integer> map) {
        int sum = 0;
        for (int i : map.values()) {
            sum += i;
        }
        return sum;
    }
}

