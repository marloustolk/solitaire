package patience.backend.logic;

import patience.backend.dto.Card;

import java.util.Collections;
import java.util.List;

public class PilePlay extends Pile {

    private List<Card> closedCards;

    public PilePlay(List<Card> cards) {
        this.closedCards = cards;
        flip();
    }

    @Override
    public boolean canPlace(List<Card> cards) {
        if (getCards().isEmpty() && closedCards.isEmpty() && cards.get(0).isKing()) {
            return true;
        }
        return !getCards().isEmpty() && cards.get(0).canStackOn(getTopCard());
    }

    @Override
    public boolean canSelect(Card card) {
        return card != null && getCards().contains(card);
    }

    @Override
    public void flip() {
        if (!closedCards.isEmpty() && getCards().isEmpty()) {
            Card card = closedCards.get(closedCards.size() - 1);
            closedCards.remove(card);
            place(Collections.singletonList(card));
        }
    }

    public List<Card> getClosedCards() {
        return closedCards;
    }

    @Override
    public void clear() {
        super.clear();
        closedCards.clear();
    }
}
