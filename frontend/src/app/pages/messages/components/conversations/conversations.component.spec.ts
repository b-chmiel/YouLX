import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConversationsComponent } from './conversations.component';
import {ActivatedRoute} from '@angular/router';

describe('ConversationsComponent', () => {
  let component: ConversationsComponent;
  let fixture: ComponentFixture<ConversationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConversationsComponent ],
      providers: [
        {
          provide: ActivatedRoute, useValue: { snapshot: { data: { conversation: { offer: { coverUrl: '' } } } } }
        }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConversationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
