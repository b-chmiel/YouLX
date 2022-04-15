import {ComponentFixture, TestBed} from '@angular/core/testing';

import {OfferComponent} from './offer.component';
import {ChangeDetectorRef} from "@angular/core";

describe('OfferComponent', () => {
  let component: OfferComponent;
  let fixture: ComponentFixture<OfferComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OfferComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferComponent);
    component = fixture.componentInstance;
    component.offer = {
      id: 'gfasd',
      title: 'Great neighbourhood, good location, big house! The best offer!',
      categories: ['property', 'house', 'new'],
      coverUrl: 'https://api.lorem.space/image/house?w=400&h=225',
      price: 600_000
    };
    fixture.detectChanges();
  });

  async function runOnPushChangeDetection(fixture: ComponentFixture<any>): Promise<void> {
    const changeDetectorRef = fixture.debugElement.injector.get<ChangeDetectorRef>(ChangeDetectorRef);
    changeDetectorRef.detectChanges();
    return fixture.whenStable();
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render offer title', () => {
    expect(fixture.nativeElement.textContent).toContain(component.offer.title);
  });

  it('should render all categories', () => {
    for (const category of component.offer.categories) {
      expect(fixture.nativeElement.textContent).toContain(category);
    }
  });

  it('should render offer price if provided', () => {
    expect(fixture.nativeElement.textContent).toContain('PLN600,000.00');
  });

  it('should render free price if no price provided', async () => {
    component.offer.price = null;
    await runOnPushChangeDetection(fixture);
    expect(fixture.nativeElement.textContent).toContain('FREE');
  });
});
