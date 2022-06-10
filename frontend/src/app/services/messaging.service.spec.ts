import { TestBed } from '@angular/core/testing';

import { MessagingService } from './messaging.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('MessagingService', () => {
  let service: MessagingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(MessagingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
