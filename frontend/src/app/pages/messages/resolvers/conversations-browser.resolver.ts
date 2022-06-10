import { Injectable } from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import {Conversation} from '../../../models/conversation';
import {MessagingService} from '../../../services/messaging.service';

@Injectable({
  providedIn: 'root'
})
export class ConversationsBrowserResolver implements Resolve<Conversation[]> {
  constructor(private service: MessagingService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Conversation[]> {
    return this.service.getBrowserConversations();
  }
}
