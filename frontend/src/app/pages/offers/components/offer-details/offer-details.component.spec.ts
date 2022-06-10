import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferDetailsComponent } from './offer-details.component';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';
import {AuthService} from '../../../../services/auth.service';

describe('OfferDetailsComponent', () => {
  let component: OfferDetailsComponent;
  let fixture: ComponentFixture<OfferDetailsComponent>;
  let offersServiceSpy: any = jasmine.createSpyObj('OffersService', ['closeOffer', 'publishOffer']);
  let authServiceSpy: any = jasmine.createSpyObj('AuthService', ['getProfileInfo']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferDetailsComponent ],
      providers: [
        {
          provide: OffersService, useValue: offersServiceSpy
        },
        {
          provide: AuthService, useValue: authServiceSpy
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              data: {
                offer: null
              }
            }
          }
        }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
