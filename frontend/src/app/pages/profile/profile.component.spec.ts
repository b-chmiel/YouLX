import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileComponent } from './profile.component';
import {ActivatedRoute} from '@angular/router';
import Profile from '../../models/profile';
import {AuthService} from '../../services/auth.service';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let authServiceSpy: any = jasmine.createSpyObj('AuthService', ['getProfileInfo']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfileComponent ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              data: {
                profile: {
                  firstName: "John",
                  lastName: "Smith",
                  login: "john",
                  email: "john.smith@email.com"
                } as Profile
              }
            }
          }
        },
        {
          provide: AuthService, useValue: authServiceSpy
        },
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
