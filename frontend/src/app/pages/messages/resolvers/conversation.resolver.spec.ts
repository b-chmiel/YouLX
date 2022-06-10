import { TestBed } from '@angular/core/testing';

import { ConversationResolver } from './conversation.resolver';
import {MessagingService} from '../../../services/messaging.service';

describe('ConversationResolver', () => {
  let resolver: ConversationResolver;
  let messagingServiceSpy: any = jasmine.createSpyObj('MessagingService', ['getMessages', 'postMessage']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: MessagingService, useValue: messagingServiceSpy}
      ]
    });
    resolver = TestBed.inject(ConversationResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
