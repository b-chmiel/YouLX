import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MessagesRoutingModule } from './messages-routing.module';
import { ConversationsComponent } from './components/conversations/conversations.component';
import { ConversationComponent } from './components/conversation/conversation.component';


@NgModule({
  declarations: [
    ConversationsComponent,
    ConversationComponent
  ],
  imports: [
    CommonModule,
    MessagesRoutingModule
  ]
})
export class MessagesModule { }
