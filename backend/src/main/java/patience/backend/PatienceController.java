package patience.backend;

import org.springframework.web.bind.annotation.*;
import patience.backend.dto.Card;
import patience.backend.dto.Symbol;
import patience.backend.logic.PatienceGame;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("cardgame/patience")
public class PatienceController {

    private PatienceGame game;

    public PatienceController(PatienceGame game) {
        game.setUp();
        this.game = game;
    }

    @GetMapping
    public String gameWelcome() {
        return String.format("<div align=\"center\"><p style=\"font-size:24px\"><br/>" +
                        "Welcome to this Patience Game!" +
                        "</p><p style=\"font-size:18px\">" +
                        "The main pile contains %s closed cards" +
                        "</p><p>%s</p></div>",
                game.getCards(PatienceGame.MAIN_PILE, true).size(),
                Symbol.htmlSpades());
    }

    @GetMapping("name")
    public String name() {
        return "Patience";
    }

    @PostMapping("start")
    public void start(@RequestBody(required = false) String becauseNeeded) {
        this.game = new PatienceGame();
    }

    @PostMapping("stop")
    public void stop(@RequestBody(required = false) String becauseNeeded) {
        this.game.clear();
    }

    @GetMapping("selection/placing")
    public Integer getSelectionPlace() {
        return this.game.getSelectionPlace();
    }

    @GetMapping("selection/card")
    public List<Card> getSelection() {
        return this.game.getSelection();
    }

    @GetMapping("placing/{placing}/card")
    public List<Card> getCards(@PathVariable("placing") Integer placing, @RequestParam("closed") boolean closed) {
        return this.game.getCards(placing, closed);
    }

    @GetMapping("placing/{placing}/card/topcard")
    public Card getTopCard(@PathVariable Integer placing, @RequestParam boolean closed) {
        return this.game.getTopCard(placing, closed);
    }

    @PostMapping("placing/{placing}/card/click")
    public void click(@PathVariable Integer placing, @RequestBody(required = false) Card card) {
        System.out.println("click!");
        this.game.click(placing, card);
        System.out.println("selection = " + this.game.getSelection());
    }

    @PostMapping("placing/{placing}/flip")
    public void flip(@PathVariable Integer placing, @RequestBody(required = false) String becauseNeeded) {
        this.game.flip(placing);
    }

    @PostMapping("placing/{placing}/card/canselect")
    public boolean canSelect(@PathVariable Integer placing, @RequestBody Card card) {
        return this.game.canSelect(placing, card);
    }

    @PostMapping("placing/{placing}/card/select")
    public void select(@PathVariable Integer placing, @RequestBody Card card) {
        this.game.select(placing, card);
    }

    @PostMapping("deselect")
    public void deselect(@RequestBody(required = false) String becauseNeeded) {
        this.game.deselect();
    }

    @PostMapping("placing/{placing}/card/canplace")
    public boolean canPlace(@PathVariable Integer placing, @RequestBody List<Card> cards) {
        return this.game.canPlace(placing, cards);
    }

    @PostMapping("placing/{placing}/card/place")
    public void place(@PathVariable Integer placing, @RequestBody List<Card> card, @RequestParam Integer placeFrom) {
        this.game.place(placing, card, placeFrom);
    }
}
