package patience.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Card {

    private Suit suit;

    private Value value;

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    @JsonCreator
    public Card(@JsonProperty("suit") String suit, @JsonProperty("value") String value) {
        this.suit = Suit.fromSymbol(suit);
        this.value = Value.fromSymbol(value);
    }

    @JsonIgnore
    public boolean isAce(){
        return value == Value.ACE;
    }

    @JsonIgnore
    public boolean isKing(){
        return value == Value.KING;
    }

    public boolean canStackOn(Card card){
        return !suit.isSameColorAs(card.suit) && value.isOneLowerThen(card.value);
    }

    public boolean canPutAwayOn(Card card){
        return suit.isSameAs(card.suit) && value.isOneHigherThen(card.value);
    }

    public String getSuit(){
        return suit.getSymbol();
    }

    public String getValue(){
        return value.getSymbol();
    }

    @Override
    public String toString(){
        return value.getSymbol() + suit.getSymbol();
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
        return suit == otherCard.suit &&
                value == otherCard.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, value);
    }
}
