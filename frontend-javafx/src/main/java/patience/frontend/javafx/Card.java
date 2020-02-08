package patience.frontend.javafx;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public class Card {

    private String suit;

    private String value;

    @JsonCreator
    public Card(@JsonProperty("suit") String suit, @JsonProperty("value") String value) {
        this.suit = suit;
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public String getValue() {
        return value;
    }

    @JsonIgnore
    boolean isRed() {
        return Arrays.asList("\u2666", "\u2665").contains(this.suit);
    }

    @Override
    public String toString() {
        return value + suit;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other){
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Card otherCard = (Card) other;
        return suit.equals(otherCard.suit) &&
                value.equals(otherCard.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, value);
    }
}
