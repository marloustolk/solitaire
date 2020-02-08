package patience.backend.logic;

import patience.backend.dto.Card;

import java.util.List;

public class PileFoundation extends Pile {

    @Override
    public boolean canPlace(List<Card> cards) {
        if (cards.size() != 1){
            return false;
        }
        if (getCards().isEmpty() && cards.get(0).isAce()){
            return true;
        } else {
            return !getCards().isEmpty() && cards.get(0).canPutAwayOn(getTopCard());
        }
    }

}
