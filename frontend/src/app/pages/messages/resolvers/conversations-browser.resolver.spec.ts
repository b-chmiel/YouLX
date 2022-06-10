import { TestBed } from '@angular/core/testing';

import { ConversationsBrowserResolver } from './conversations-browser.resolver';
import {MessagingService} from '../../../services/messaging.service';

describe('ConversationsBrowserResolver', () => {
  let resolver: ConversationsBrowserResolver;
  let messagingServiceSpy: any = jasmine.createSpyObj('MessagingService', ['getMessages', 'postMessage']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: MessagingService, useValue: messagingServiceSpy}
      ]
    });
    resolver = TestBed.inject(ConversationsBrowserResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
