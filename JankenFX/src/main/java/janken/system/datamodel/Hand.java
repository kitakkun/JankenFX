package janken.system.datamodel;

/**
 * じゃんけんの手を管理する列挙型
 * */
public enum Hand {
    ROCK("Rock", 0b100), PAPER("Paper", 0b001), SCISSORS("Scissors", 0b010);

    private final String label;
    private final int id;

    Hand(String label, int id) {
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
