import {Injectable} from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot,
} from '@angular/router';
import {Observable} from 'rxjs';
import {Message} from '../../../models/conversation';
import {MessagingService} from '../../../services/messaging.service';

@Injectable({
  providedIn: 'root',
})
export class ConversationResolver implements Resolve<Message[]> {
  constructor(private service: MessagingService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Message[]> {
    const conversationId = route.paramMap.get('conversationId');
    return this.service.getMessages(conversationId!);
  }
}
