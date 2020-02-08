package patience.backend.logic;

import patience.backend.dto.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PileMain extends Pile {

    private List<Card> closedCards;

    public PileMain(List<Card> cards) {
        closedCards = new ArrayList<>(cards);
    }

    @Override
    public void flip() {
        if (closedCards.isEmpty()) {
            closedCards = new ArrayList<>(getCards());
            empty();
            return;
        }

        Card card = closedCards.get(closedCards.size() - 1);
        closedCards.remove(card);
        place(Collections.singletonList(card));
    }

    public List<Card> getClosedCards() {
        return closedCards;
    }

    @Override
    public boolean canSelect(Card card) {
        return card != null && card.equals(getTopCard());
    }


    @Override
    public void clear() {
        super.clear();
        closedCards.clear();
    }
}
