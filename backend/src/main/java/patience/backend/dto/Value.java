package patience.backend.dto;

public enum Value {
    ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"),
    SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K");

    private final String symbol;

    Value(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Value fromSymbol(String symbol) {
        for (Value value : Value.values()) {
            if (value.symbol.equals(symbol)) {
                return value;
            }
        }
        throw new IllegalStateException("No Value found for symbol " + symbol);
    }

    boolean isOneLowerThen(Value value) {
        return this.ordinal() == value.ordinal() - 1;
    }

    boolean isOneHigherThen(Value value) {
        return this.ordinal() == value.ordinal() + 1;
    }
}
