import { Component, EventEmitter, HostBinding, Input, OnInit, Output } from '@angular/core';
import { Card } from '../../model/card';

@Component({
  selector: 'app-card',
  template: `
            <div *ngIf="closed else showCard" class="card main" (click)="click(card)">
              <p class="top"></p>
              <p class="center main">{{total}}</p>
              <p class="bottom"></p>
            </div>
            <ng-template #showCard>
              <div class="card" (click)="click(card)" *ngIf="isSelected(card) else showUnselectedCard"
              [style.border]="getBorder(card)"
              [style.background-color]="getBackgroundColor(card)"
              [style.color]="getColor(card)"
              [style.margin-left]="getMarginLeft(card)"
              [style.width]="getWidth(card)">
                <p class="top">{{card.suit + card.value}}</p>
                <p class="center">{{card.suit + card.value}}</p>
                <p class="bottom">{{card.suit + card.value}}</p>
              </div>
            <ng-template #showUnselectedCard>
              <div class="card" (click)="click(card)"
              [style.border]="getBorder(card)"
              [style.color]="getColor(card)">
                <p class="top">{{card.suit + card.value}}</p>
                <p class="center">{{card.suit + card.value}}</p>
                <p class="bottom">{{card.suit + card.value}}</p>
              </div>
            </ng-template>
            </ng-template>
            `,
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {
@Input() card: Card;
@Input() index: number = 0;
@Input() selection: Card[];
@Input() closed: boolean;
@Input() total: number;

@Output() clickEvent: EventEmitter<Card> = new EventEmitter();

@HostBinding('style.z-index') zIndex;
@HostBinding('style.grid-row-start') rowStart;
@HostBinding('style.grid-row-end') rowEnd;

  constructor() { }

  ngOnInit() {
    this.zIndex = (this.index + 1).toString();
    this.rowStart = this.zIndex;
    this.rowEnd = (this.index + 7).toString();
  }

  getBackgroundColor(card): string {
    return "#ffffff";
  }

  getBorder(card): string {
    return this.isSelected(card) ? "2px solid #b01030" : "1px solid #556";
  }

  getColor(card): string {
    let cardString = card.suit.concat(card.value);
    return (cardString.includes("♥") || cardString.includes("♦")) ? "#b01030" : "#000000";
  }

  getMarginLeft(card): string {
    return "-2.5%";
  }

  getWidth(card): string {
    return "100%";
  }

  isSelected(card): boolean {
    if (this.selection == null || !this.selection.length){
      return false;
    }
    for (const cardToMatch of this.selection) {
      if (card.suit.concat(card.value) === cardToMatch.suit.concat(cardToMatch.value)){
        return true;
      }
    }
    return false;
  }

  click(card){
    this.clickEvent.emit(card);
  }

}
