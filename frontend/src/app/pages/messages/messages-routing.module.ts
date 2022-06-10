import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ConversationsComponent} from './components/conversations/conversations.component';
import {ConversationsResolver} from './resolvers/conversations.resolver';
import {ConversationComponent} from './components/conversation/conversation.component';
import {ConversationResolver} from './resolvers/conversation.resolver';

const routes: Routes = [
  {
    path: 'conversations',
    component: ConversationsComponent,
    resolve: {
      conversations: ConversationsResolver
    }
  },
  {
    path: 'conversations/:conversationId',
    component: ConversationComponent,
    resolve: {
      messages: ConversationResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MessagesRoutingModule { }
