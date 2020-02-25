import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Place} from "../model/place";
import {Card} from "../model/card";
import {Observable, throwError} from "rxjs";
import { catchError, retry } from 'rxjs/operators';

@Injectable()
export class GameService {

  private url: string = 'http://localhost:8080/cardgame/patience';
  constructor(private http: HttpClient) {
  }

  public getName(): Observable<String> {
    return this.http.get(this.url + "/name", { responseType: 'text' }).pipe(catchError(this.handleError));
  }

  public start() {
    return this.http.post(this.url + "/start", {}).pipe(catchError(this.handleError));
  }

  public stop() {
    return this.http.post(this.url + "/stop", {}).pipe(catchError(this.handleError));
  }

  public getTopCard(placing: Place, closed:boolean): Observable<Card> {
    let getUrl = this.url + "/placing/" + Number(placing) + "/card/topcard?closed=" + closed;
    return this.http.get<Card>(getUrl).pipe(catchError(this.handleError));
  }

  public getCards(placing: Place, closed:boolean): Observable<Card[]> {
    let getUrl = this.url + "/placing/" + Number(placing) + "/card?closed=" + closed;
    return this.http.get<Card[]>(getUrl).pipe(catchError(this.handleError));
  }

  public flip(placing: Place): Observable<Object> {
    let postUrl = this.url + "/placing/" + Number(placing) + "/flip";
    return this.http.post(postUrl, {}).pipe(catchError(this.handleError));
  }

  public click(placing: Place, card: Card): Observable<Object> {
      let postUrl = this.url + "/placing/" + Number(placing) + "/card/click";
      return this.http.post(postUrl, card).pipe(catchError(this.handleError));
  }

  public getSelection(): Observable<Card[]> {
    let getUrl = this.url + "/selection/card";
    return this.http.get<Card[]>(getUrl).pipe(catchError(this.handleError));
  }

  public getSelectionPlace(): Observable<number> {
      let getUrl = this.url + "/selection/placing";
      return this.http.get<number>(getUrl).pipe(catchError(this.handleError));
    }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError('No connection; please try again later.');
  };

}
