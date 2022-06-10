import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Conversation} from '../../../../models/conversation';

@Component({
  selector: 'app-conversations',
  templateUrl: './conversations.component.html',
  styleUrls: ['./conversations.component.scss']
})
export class ConversationsComponent implements OnInit {
  conversationsPoster!: Conversation[];
  conversationsBrowser!: Conversation[];

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.conversationsPoster = this.route.snapshot.data["conversationsPoster"];
    this.conversationsBrowser = this.route.snapshot.data["conversationsBrowser"];
  }

}
