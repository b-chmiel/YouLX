import {AfterViewInit, Component, ElementRef, OnInit, ViewChild, ViewRef} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Conversation, Message} from '../../../../models/conversation';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MessagingService} from '../../../../services/messaging.service';
import {filter, switchMap} from 'rxjs';

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.scss'],
})
export class ConversationComponent implements OnInit, AfterViewInit {
  conversation!: Conversation;
  messages!: Message[];
  message = new FormGroup({
    content: new FormControl("", Validators.required)
  });
  @ViewChild("chat") chat!: ElementRef;

  constructor(private route: ActivatedRoute, private messaging: MessagingService) {
  }

  ngOnInit(): void {
    this.conversation = this.route.snapshot.data['conversation'];
    this.messages = this.route.snapshot.data['messages'];
  }

  ngAfterViewInit() {
    this.scrollToBottom();
  }

  addComment() {
    if (!this.message.valid) {
      return;
    }

    const message = this.message.value;
    this.messaging.postMessage(this.conversation.id, message.content).pipe(
      filter(Boolean),
      switchMap(_ => this.messaging.getMessages(this.conversation.id))
    ).subscribe(messages => {
      this.messages = messages;
      this.message.reset();
      this.scrollToBottom();
    })
  }

  scrollToBottom(): void {
    try {
      this.chat.nativeElement.scrollTop = this.chat.nativeElement.scrollHeight;
    } catch(err) { }
  }
}
