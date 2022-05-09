import {ComponentFixture, TestBed} from '@angular/core/testing';

import {OfferNewComponent} from './offer-new.component';
import {AuthService} from '../../../../services/auth.service';
import {OffersService} from '../../../../services/offers.service';
import {Router} from '@angular/router';
import {RouterTestingModule} from '@angular/router/testing';

describe('OfferNewComponent', () => {
  let component: OfferNewComponent;
  let fixture: ComponentFixture<OfferNewComponent>;
  let offersServiceSpy: any = jasmine.createSpyObj('OffersService', ['createOffer']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OfferNewComponent],
      imports: [
        RouterTestingModule.withRoutes([]),
      ],
      providers: [
        {provide: OffersService, useValue: offersServiceSpy},
      ],
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
