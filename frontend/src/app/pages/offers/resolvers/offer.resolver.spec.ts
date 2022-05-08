import { TestBed } from '@angular/core/testing';

import { OfferResolver } from './offer.resolver';
import {OffersService} from '../../../services/offers.service';
import {of} from 'rxjs';

describe('OfferResolver', () => {
  let resolver: OfferResolver;
  let offersServiceSpy: any = jasmine.createSpyObj('OffersService', ['getOffer']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: OffersService,
          useValue: offersServiceSpy,
        },
      ],
    });
    resolver = TestBed.inject(OfferResolver);
  });

  it('should be created', () => {
    offersServiceSpy.getOffer.and.returnValue(of(null));
    expect(resolver).toBeTruthy();
  });
});
