import { TestBed } from '@angular/core/testing';

import { ConversationsPosterResolver } from './conversations-poster.resolver';

describe('ConversationsPosterResolver', () => {
  let resolver: ConversationsPosterResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(ConversationsPosterResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
