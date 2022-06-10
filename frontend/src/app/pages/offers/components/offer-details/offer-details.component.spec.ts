import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferDetailsComponent } from './offer-details.component';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';
import {AuthService} from '../../../../services/auth.service';
import {MessagingService} from '../../../../services/messaging.service';
import {RouterTestingModule} from '@angular/router/testing';

describe('OfferDetailsComponent', () => {
  let component: OfferDetailsComponent;
  let fixture: ComponentFixture<OfferDetailsComponent>;
  let offersServiceSpy: any = jasmine.createSpyObj('OffersService', ['closeOffer', 'publishOffer']);
  let authServiceSpy: any = jasmine.createSpyObj('AuthService', ['getProfileInfo']);
  let messagingServiceSpy: any = jasmine.createSpyObj('MessagingService', ['getMessages', 'postMessage']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferDetailsComponent ],
      imports: [
        RouterTestingModule
      ],
      providers: [
        {
          provide: OffersService, useValue: offersServiceSpy
        },
        {
          provide: AuthService, useValue: authServiceSpy
        },
        {
          provide: MessagingService, useValue: messagingServiceSpy
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
