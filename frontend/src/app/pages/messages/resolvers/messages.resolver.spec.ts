import { TestBed } from '@angular/core/testing';

import { MessagesResolver } from './messages.resolver';
import {MessagingService} from '../../../services/messaging.service';

describe('MessagesResolver', () => {
  let resolver: MessagesResolver;
  let messagingServiceSpy: any = jasmine.createSpyObj('MessagingService', ['getMessages', 'postMessage']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: MessagingService, useValue: messagingServiceSpy}
      ]
    });
    resolver = TestBed.inject(MessagesResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
