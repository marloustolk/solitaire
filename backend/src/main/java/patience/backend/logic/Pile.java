package patience.backend.logic;

import patience.backend.dto.Card;
import patience.backend.dto.Suit;
import patience.backend.dto.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Pile {

    private List<Card> cards = new ArrayList<>();

    public Pile() {
        // default
    }

    public Pile(List<Card> cards) {
        this.cards = cards;
    }

    public Card getTopCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(cards.size() - 1);
    }

    public boolean canSelect(Card card) {
        return false;
    }

    public Card getAndRemoveTopCard() {
        Card card = getTopCard();
        cards.remove(card);
        return card;
    }

    public List<Card> getAndRemoveCards(List<Card> selectionCards) {
        if (!selectionCards.get(selectionCards.size() - 1).equals(getTopCard())) {
            throw new IllegalStateException();
        }
        List<Card> sublist = new ArrayList<>(cards.subList(cards.indexOf(selectionCards.get(0)), cards.size()));
        cards.removeAll(sublist);
        return sublist;
    }

    public void place(List<Card> newCards) {
        cards.addAll(newCards);
    }

    public void clear() {
        cards.clear();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void empty() {
        this.cards = new ArrayList<>();
    }

    public boolean canPlace(List<Card> card) {
        return false;
    }

    public void flip() {
        // do nothing
    }

    public static Pile getShuffledCards() {
        List<Card> cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            Arrays.stream(Value.values()).forEach(value -> cards.add(new Card(suit, value)));
        }
        Collections.shuffle(cards);
        return new Pile(cards);
    }
}
