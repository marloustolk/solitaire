import { Component, OnInit } from '@angular/core';
import {GameService} from "../../service/game.service";

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {
  error;

  constructor(private gameService: GameService) { }

  ngOnInit() {
    this.gameService.getName().subscribe(data => data, error => this.error = error);
  }

  hasError(): boolean {
    return !(this.error == null || this.error === null);
  }

}
