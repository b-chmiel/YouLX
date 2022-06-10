import {Injectable} from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot,
} from '@angular/router';
import {map, Observable, of} from 'rxjs';
import {Conversation} from '../../../models/conversation';
import {MessagingService} from '../../../services/messaging.service';

@Injectable({
  providedIn: 'root',
})
export class ConversationResolver implements Resolve<Conversation> {
  constructor(private service: MessagingService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Conversation> {
    const conversationId = route.paramMap.get('conversationId');
    return this.service.getConversations().pipe(map(conversations => {
      return conversations.filter(conversation => conversation.id == conversationId)[0];
    }));
  }
}
