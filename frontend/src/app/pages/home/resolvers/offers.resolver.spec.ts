import {TestBed} from '@angular/core/testing';

import {OffersResolver} from './offers.resolver';
import {OffersService} from '../../../services/offers.service';
import {of} from 'rxjs';

describe('OffersResolver', () => {
  let resolver: OffersResolver;
  let offersServiceSpy: any = jasmine.createSpyObj('OffersService', ['getOffers']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: OffersService,
          useValue: offersServiceSpy,
        },
      ],
    });
    resolver = TestBed.inject(OffersResolver);
  });

  it('should be created', () => {
    offersServiceSpy.getOffers.and.returnValue(of([]));
    expect(resolver).toBeTruthy();
  });
});
