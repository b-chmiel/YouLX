import { TestBed } from '@angular/core/testing';

import { ConversationsPosterResolver } from './conversations-poster.resolver';
import {MessagingService} from '../../../services/messaging.service';

describe('ConversationsPosterResolver', () => {
  let resolver: ConversationsPosterResolver;
  let messagingServiceSpy: any = jasmine.createSpyObj('MessagingService', ['getMessages', 'postMessage']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: MessagingService, useValue: messagingServiceSpy}
      ]
    });
    resolver = TestBed.inject(ConversationsPosterResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
