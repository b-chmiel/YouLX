import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConversationComponent } from './conversation.component';
import {MessagingService} from '../../../../services/messaging.service';
import {ActivatedRoute} from '@angular/router';

describe('ConversationComponent', () => {
  let component: ConversationComponent;
  let fixture: ComponentFixture<ConversationComponent>;
  let messagingServiceSpy: any = jasmine.createSpyObj('MessagingService', ['getMessages', 'postMessage']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConversationComponent ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              data: {
                messages: [],
                conversation: {
                  offer: { coverUrl: '', user: { login: '' } },
                }
              }
            }
          }
        },
        { provide: MessagingService, useValue: messagingServiceSpy }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConversationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
