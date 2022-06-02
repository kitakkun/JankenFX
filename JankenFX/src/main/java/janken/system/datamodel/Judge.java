package janken.system.datamodel;

/**
 * 勝敗の判定を区別するための列挙型
 */
public enum Judge {
    WIN("WIN", 0b001), LOSE("LOSE", 0b010), DRAW("DRAW", 0b100);

    private final String label;
    private final int id;
    Judge(String label, int id) {
        this.label = label;
        this.id = id;
    }

    @Override
    public String toString() {
        return label;
    }

    public int getId() {
        return id;
    }
}
