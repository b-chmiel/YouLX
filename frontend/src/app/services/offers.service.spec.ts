import { TestBed } from '@angular/core/testing';

import { OffersService } from './offers.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

describe('OffersService', () => {
  let service: OffersService;
  let httpTest: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(OffersService);
    httpTest = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
