import { Component, OnInit } from '@angular/core';
import { GameService } from "../../service/game.service";
import { Place } from "../../model/place";
import { Card } from "../../model/card";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit {
  openCardPiles: Map<Place, Card[]>;
  closedCardPiles: Map<Place, Card[]>;
  selection: Card[];
  selectionPlace: number;

  mainPile: Place = Place.MAIN;
  foundationPiles = [Place.FOUNDATION_1, Place.FOUNDATION_2, Place.FOUNDATION_3, Place.FOUNDATION_4];
  playPiles = [Place.PLAY_1, Place.PLAY_2, Place.PLAY_3, Place.PLAY_4, Place.PLAY_5, Place.PLAY_6, Place.PLAY_7];

  constructor(private gameService: GameService) {}

  ngOnInit() {
      this.closedCardPiles = new Map<Place, Card[]>();
      this.openCardPiles = new Map<Place, Card[]>();
      Object.keys(Place).filter(key => isNaN(Number(Place[key]))).forEach(place => {
        this.closedCardPiles.set(Number(place), []);
        this.openCardPiles.set(Number(place), []);
      });
      Object.keys(Place).filter(key => isNaN(Number(Place[key]))).forEach(place => {
        this.gameService.getCards(Number(place), true)
          .subscribe(data => this.closedCardPiles.set(Number(place), data));
        this.gameService.getCards(Number(place), false)
          .subscribe(data => this.openCardPiles.set(Number(place), data));
      });
      this.gameService.getSelection().subscribe(data => this.selection = data);
      this.gameService.getSelectionPlace().subscribe(data => this.selectionPlace = data);
  }

  refresh(place: Place): void {
    console.log("refresh " + place);
    this.gameService.getSelection().subscribe(data => this.selection = data);
    this.gameService.getSelectionPlace().subscribe(data => this.selectionPlace = data);
    this.gameService.getCards(place, true).subscribe(data => this.closedCardPiles.set(place, data));
    this.gameService.getCards(place, false).subscribe(data => this.openCardPiles.set(place, data));
  }

  start(): void {
   console.log("refresh all!");
   this.gameService.start().subscribe();
   Object.keys(Place).filter(key => isNaN(Number(Place[key]))).forEach(place => {
     this.refresh(Number(place));
   });
  }

  clear(): void {
   console.log("clear");
   this.gameService.stop().subscribe();
      Object.keys(Place).filter(key => isNaN(Number(Place[key]))).forEach(place => {
        this.refresh(Number(place));
      });
  }

  flip(place: Place): void{
    this.gameService.flip(place).subscribe();
    this.refresh(place);
  }

  click(place: Place, card: Card){
      this.gameService.click(place, card).subscribe();
      if (this.selectionPlace != null) {
        this.refresh(Place[Place[this.selectionPlace]]);
      }
      this.refresh(place);
  }

  getOpenCards(place:Place): Card[] {
    return this.openCardPiles.get(place);
  }

  getClosedCards(place:Place): Card[] {
    return this.closedCardPiles.get(place);
  }

  getTopCard(place:Place) {
    let pile = this.openCardPiles.get(place);
    return (pile == null || pile.length == 0) ? null : pile[pile.length -1];
  }

  hasOpenCards(place:Place): boolean {
    return this.hasCards(this.getOpenCards(place));
  }

  hasClosedCards(place:Place): boolean {
    return this.hasCards(this.getClosedCards(place));
  }

  hasCards(pile: Card[]): boolean {
    return !(pile == null || pile.length == 0);
  }


  getClosedCardsSize(place:Place): number {
    return this.getClosedCards(place).length;
  }

  getColor(card: Card) {
    let cardString = card.suit.concat(card.value);
    return (cardString.includes("♥") || cardString.includes("♦")) ? "#b01030" : "#000000";
  }

  getBorder(card: Card) {
    if (this.selection == null || this.selection.length == 0){
      return "1px solid #000000";
    }
    for (const cardToMatch of this.selection) {
      if (card.suit.concat(card.value) === cardToMatch.suit.concat(cardToMatch.value)){
        return "2px solid #b01030";
      }
    }
    return "1px solid #000000";
  }

  getClassClosedCard(index: number): string{
    return "card main nr".concat((index + 1).toString());
  }

  getClassOpenCard(place:Place, index: number): string{
    let nr = this.getClosedCards(place).length;
    return "card nr".concat((nr + index + 1).toString());
  }

  getClassPlayPile(index:number, closed:boolean): string {
    let nr = " nr".concat((index + 1).toString());
    return closed ? "card main".concat(nr) : "card ".concat(nr);
  }

  getPlayPileCards(place:Place): Map<Card, boolean> {
    let map = new Map<Card, boolean>();
    this.getClosedCards(place).forEach(closedCard => map.set(closedCard, true));
    this.getOpenCards(place).forEach(openCard => map.set(openCard, false));
    return map;
  }
}
