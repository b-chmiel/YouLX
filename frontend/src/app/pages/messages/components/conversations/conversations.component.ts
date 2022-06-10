import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Conversation} from '../../../../models/conversation';

@Component({
  selector: 'app-conversations',
  templateUrl: './conversations.component.html',
  styleUrls: ['./conversations.component.scss']
})
export class ConversationsComponent implements OnInit {
  conversations!: Conversation[];

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.conversations = this.route.snapshot.data["conversations"];
  }

}
