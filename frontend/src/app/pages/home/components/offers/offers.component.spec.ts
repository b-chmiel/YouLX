import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OffersComponent } from './offers.component';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';
import {of} from 'rxjs';

describe('OffersComponent', () => {
  let component: OffersComponent;
  let fixture: ComponentFixture<OffersComponent>;
  let offersServiceSpy: any = jasmine.createSpyObj('OffersService', ['getOffers']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OffersComponent ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              data: {
                offers: []
              }
            }
          }
        },
        {
          provide: OffersService,
          useValue: offersServiceSpy,
        },
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OffersComponent);
    offersServiceSpy.getOffers.and.returnValue(of([]));
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
