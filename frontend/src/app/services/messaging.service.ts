import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, combineLatest, concatMap, filter, map, Observable, of, zip} from 'rxjs';
import {Conversation, Message} from '../models/conversation';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class MessagingService {
  private readonly apiPrefix = "/api/conversations"

  constructor(private http: HttpClient, private auth: AuthService) {
  }

  createConversationForOffer(offerId: string): Observable<string | null> {
    return this.http.post<Conversation>(this.apiPrefix + "/" + offerId, {}).pipe(
      map(conversation => conversation.id),
      catchError(_ => of(null))
    )
  }

  getConversations(): Observable<Conversation[]> {
    return this.http.get<Conversation[]>(this.apiPrefix).pipe(catchError(_ => of([])));
  }

  getBrowserConversations(): Observable<Conversation[]> {
    return this.getConversations();
  }

  getPosterConversations(): Observable<Conversation[]> {
    return this.getConversations();
  }

  getMessages(conversationId: string): Observable<Message[]> {
    return this.http.get<Message[]>(this.apiPrefix + "/" + conversationId + "/messages").pipe(
      catchError(_ => of([]))
    )
  }

  postMessage(conversationId: string, content: string): Observable<boolean> {
    return this.http.post(this.apiPrefix + "/" + conversationId + "/messages", {content}, {observe: 'response'}).pipe(
      map(response => response.ok)
    )
  }
}
