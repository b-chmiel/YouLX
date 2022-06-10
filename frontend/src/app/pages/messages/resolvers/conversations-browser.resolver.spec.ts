import { TestBed } from '@angular/core/testing';

import { ConversationsBrowserResolver } from './conversations-browser.resolver';

describe('ConversationsBrowserResolver', () => {
  let resolver: ConversationsBrowserResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(ConversationsBrowserResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
