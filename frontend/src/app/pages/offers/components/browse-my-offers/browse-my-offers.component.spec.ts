import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseMyOffersComponent } from './browse-my-offers.component';
import {OffersService} from '../../../../services/offers.service';
import {of} from 'rxjs';
import {ActivatedRoute} from '@angular/router';

describe('BrowseMyOffersComponent', () => {
  let component: BrowseMyOffersComponent;
  let fixture: ComponentFixture<BrowseMyOffersComponent>;
  let offersServiceSpy: any = jasmine.createSpyObj('OffersService', ['getOwnOffers']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BrowseMyOffersComponent ],
      providers: [
        {
          provide: OffersService,
          useValue: offersServiceSpy,
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              data: {
                offers: {
                  _embedded: {
                    offers: []
                  },
                  page: {
                    number: 0,
                    size: 0,
                    totalPages: 0,
                    totalElements: 0
                  }
                }
              }
            }
          }
        }
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowseMyOffersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    offersServiceSpy.getOwnOffers.and.returnValue(of({
      _embedded: {
        offers: []
      },
      page: {
        number: 0,
        size: 0,
        totalPages: 0,
        totalElements: 0
      }
    }));
    expect(component).toBeTruthy();
  });
});
