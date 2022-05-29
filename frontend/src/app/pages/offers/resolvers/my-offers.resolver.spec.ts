import { TestBed } from '@angular/core/testing';

import { MyOffersResolver } from './my-offers.resolver';
import {OffersService} from '../../../services/offers.service';
import {of} from 'rxjs';

describe('MyOffersResolver', () => {
  let resolver: MyOffersResolver;
  let offersServiceSpy: any = jasmine.createSpyObj('OffersService', ['getOwnOffers']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: OffersService,
          useValue: offersServiceSpy,
        },
      ],
    });
    resolver = TestBed.inject(MyOffersResolver);
  });

  it('should be created', () => {
    offersServiceSpy.getOwnOffers.and.returnValue(of([]));
    expect(resolver).toBeTruthy();
  });
});
