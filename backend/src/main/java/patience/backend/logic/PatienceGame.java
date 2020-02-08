package patience.backend.logic;

import org.springframework.stereotype.Component;
import patience.backend.dto.Card;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class PatienceGame {

    public static final int MAIN_PILE = 0;

    public static final List<Integer> FOUNDATION_PILES = Arrays.asList(1, 2, 3, 4);

    public static final List<Integer> PLAY_PILES = Arrays.asList(5, 6, 7, 8, 9, 10, 11);

    private final Map<Integer, Pile> piles = new HashMap<>();

    private final List<Card> selection = new ArrayList<>();

    private Integer selectionPlace;

    public PatienceGame() {
        setUp();
    }

    public void setUp() {
        Pile pile = Pile.getShuffledCards();
        FOUNDATION_PILES.forEach(foundationPile -> piles.put(foundationPile, new PileFoundation()));
        PLAY_PILES.forEach(playPile -> {
            List<Card> cards = new ArrayList<>();
            int numberOfCards = playPile - 4;
            IntStream.range(0, numberOfCards).forEach(number -> cards.add(pile.getAndRemoveTopCard()));
            piles.put(playPile, new PilePlay(cards));
        });
        piles.put(MAIN_PILE, new PileMain(pile.getCards()));
    }

    public void clear(){
        piles.keySet().forEach(key -> piles.get(key).clear());
        selection.clear();
        selectionPlace = null;
    }

    public void flip(int placing){
        piles.get(placing).flip();
    }

    public void click(int placing, Card card) {
        // if there is no selection and card can be selected -> select
        if (selection.isEmpty() && canSelect(placing, card)) {
            select(placing, card);

            // if there is a selection that can be placed -> place
        } else if (!selection.isEmpty() && canPlace(placing, selection)) {
            place(placing, selection, getSelectionPlace());
            deselect();

            // if there is a selection that can not be placed -> deselect
        } else if (!selection.isEmpty()) {
            deselect();
        }
    }

    public boolean canSelect(int placing, Card card){
        return piles.get(placing).canSelect(card);
    }

    public void deselect() {
        selection.clear();
        selectionPlace = null;
    }

    public void select(int place, Card card) {
        selection.addAll(makeSelection(place, card));
        selectionPlace = place;
    }

    public boolean canPlace(int placing, List<Card> selection){
        return piles.get(placing).canPlace(selection);
    }

    public void place(int placing, List<Card> selection, int fromPlacing){
        piles.get(placing).place(piles.get(fromPlacing).getAndRemoveCards(selection));
    }
    private List<Card> makeSelection(int placing, Card card){
        List<Card> cards = getCards(placing, false);
        return new ArrayList<>(cards.subList(cards.indexOf(card), cards.size()));
    }

    public List<Card> getCards(int placing, boolean closedCards) {
        if (closedCards && placing == MAIN_PILE) {
            return ((PileMain) piles.get(placing)).getClosedCards();
        }
        if (closedCards && PLAY_PILES.contains(placing)) {
            return ((PilePlay) piles.get(placing)).getClosedCards();
        }
        return piles.get(placing).getCards();
    }

    public List<Card> getSelection() {
        return selection;
    }

    public Integer getSelectionPlace() {
        return selectionPlace;
    }

    public Card getTopCard(int placing, boolean closedCards) {
        List<Card> cards = getCards(placing, closedCards);
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(cards.size() - 1);
    }
}
