import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {GameService} from "./service/game.service";
import { HttpClientModule } from '@angular/common/http';
import { WelcomeComponent } from './component/welcome/welcome.component';
import { BoardComponent } from './component/board/board.component';
import { ButtonsComponent } from './component/buttons/buttons.component';
import { CardComponent } from './component/card/card.component';
//import { DragDropModule } from '@angular/cdk/drag-drop';

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    BoardComponent,
    ButtonsComponent,
    CardComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    //DragDropModule,
    AppRoutingModule
  ],
  providers: [GameService],
  bootstrap: [AppComponent]
})
export class AppModule { }
