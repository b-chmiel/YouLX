import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, combineLatest, concatMap, filter, map, Observable, of, switchMap, withLatestFrom, zip} from 'rxjs';
import {Conversation, Message} from '../models/conversation';
import {AuthService} from './auth.service';
import Profile from '../models/profile';

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
    return this.getConversations().pipe(
      withLatestFrom(this.auth.getProfileInfo()),
      map(x => {
        const [conversations, profile]: [Conversation[], Profile | null] = x;
        if (!profile) {
          return [];
        }
        return conversations.filter(conversation => conversation.posterId != profile.login);
      })
    );
  }

  getPosterConversations(): Observable<Conversation[]> {
    return this.getConversations().pipe(
      withLatestFrom(this.auth.getProfileInfo()),
      map(x => {
        const [conversations, profile]: [Conversation[], Profile | null] = x;
        if (!profile) {
          return [];
        }
        return conversations.filter(conversation => conversation.posterId == profile.login);
      })
    );
  }

  getMessages(conversationId: string): Observable<Message[]> {
    return this.http.get<Message[]>(this.apiPrefix + "/" + conversationId + "/messages").pipe(
      catchError(_ => of([])),
      withLatestFrom(this.auth.getProfileInfo()),
      map(x => {
        const [messages, profile] = x;
        if (!profile) {
          return [];
        }
        return messages.map(message => {
          if (message.userId == profile.login) {
            message.owned = true;
          }
          return message;
        });
      })
    )
  }

  postMessage(conversationId: string, content: string): Observable<boolean> {
    const body = {
      content: content
    };
    return this.http.post(this.apiPrefix + "/" + conversationId + "/messages", body, {observe: 'response'}).pipe(
      map(response => response.ok)
    )
  }
}
