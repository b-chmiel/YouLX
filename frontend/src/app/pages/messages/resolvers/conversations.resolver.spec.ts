import { TestBed } from '@angular/core/testing';

import { ConversationsResolver } from './conversations.resolver';

describe('ConversationsResolver', () => {
  let resolver: ConversationsResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(ConversationsResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
