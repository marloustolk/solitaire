package patience.backend.dto;

import java.util.Arrays;

public enum Suit {
    SPADES("\u2660"),
    DIAMONDS("\u2666"),
    CLUBS("\u2663"),
    HEARTS("\u2665");

    private final String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Suit fromSymbol(String symbol){
        for (Suit suit: Suit.values()){
            if (suit.symbol.equals(symbol)){
                return suit;
            }
        }
        throw new IllegalStateException("No Suit found for symbol " + symbol);
    }

    public boolean isSameAs(Suit suit) {
        return this == suit;
    }

    public boolean isRed() { return Arrays.asList(DIAMONDS, HEARTS).contains(this); }

    public boolean isSameColorAs(Suit suit) {
        return this.isRed() == suit.isRed();
    }
}
