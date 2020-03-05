import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { GameService } from "../../service/game.service";

@Component({
  selector: 'app-buttons',
  template: `
              <p class="buttons">
                <button (click)="start()">new game</button>
                <button (click)="stop()">stop game</button>
              </p>
              `,
  styleUrls: ['./buttons.component.css']
})
export class ButtonsComponent implements OnInit {
  @Output() startEvent: EventEmitter<any> = new EventEmitter();
  @Output() stopEvent: EventEmitter<any> = new EventEmitter();

  constructor(private gameService: GameService) { }

  ngOnInit() {
  }

  start(){
    this.startEvent.emit();
  }

  stop(){
    this.stopEvent.emit();
  }

}
