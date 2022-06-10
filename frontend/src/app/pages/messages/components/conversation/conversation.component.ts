import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Message} from '../../../../models/conversation';

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.scss'],
})
export class ConversationComponent implements OnInit {
  messages!: Message[];

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.messages = this.route.snapshot.data['messages'];
  }

}
