import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ConversationsComponent} from './components/conversations/conversations.component';
import {ConversationComponent} from './components/conversation/conversation.component';
import {MessagesResolver} from './resolvers/messages.resolver';
import {ConversationsPosterResolver} from './resolvers/conversations-poster.resolver';
import {ConversationsBrowserResolver} from './resolvers/conversations-browser.resolver';
import {ConversationResolver} from './resolvers/conversation.resolver';

const routes: Routes = [
  {
    path: 'conversations',
    component: ConversationsComponent,
    resolve: {
      conversationsPoster: ConversationsPosterResolver,
      conversationsBrowser: ConversationsBrowserResolver
    }
  },
  {
    path: 'conversations/:conversationId',
    component: ConversationComponent,
    resolve: {
      conversation: ConversationResolver,
      messages: MessagesResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MessagesRoutingModule { }
