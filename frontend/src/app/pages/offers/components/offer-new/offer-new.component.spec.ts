import {ComponentFixture, TestBed} from '@angular/core/testing';

import {OfferNewComponent} from './offer-new.component';
import {AuthService} from '../../../../services/auth.service';
import {OffersService} from '../../../../services/offers.service';
import {Router} from '@angular/router';
import {RouterTestingModule} from '@angular/router/testing';
import {TagsService} from '../../../../services/tags.service';
import {MatAutocompleteModule} from '@angular/material/autocomplete';

describe('OfferNewComponent', () => {
  let component: OfferNewComponent;
  let fixture: ComponentFixture<OfferNewComponent>;
  let offersServiceSpy: any = jasmine.createSpyObj('OffersService', ['createOffer']);
  let tagsServiceSpy: any = jasmine.createSpyObj('TagsService', ['getTags']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OfferNewComponent],
      imports: [
        RouterTestingModule.withRoutes([]),
        MatAutocompleteModule
      ],
      providers: [
        {provide: OffersService, useValue: offersServiceSpy},
        {provide: TagsService, useValue: tagsServiceSpy}
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
